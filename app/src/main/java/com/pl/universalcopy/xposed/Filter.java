package com.pl.universalcopy.xposed;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface Filter {

  boolean filter(View view);

  String getContent(View view);

  class TextViewValidFilter implements Filter {

    @Override public boolean filter(View view) {
      return (view instanceof TextView) && !(view instanceof EditText);
      //            return (view instanceof TextView || view instanceof AppCompatTextView) && !(view instanceof Button)
      //                    && !(view instanceof EditText)
      //                    && !(view instanceof CheckedTextView)
      //                    && !(view instanceof DigitalClock)
      //                    && !(view instanceof Chronometer);
    }

    @Override public String getContent(View view) {
      if (view instanceof TextView) {
        CharSequence text = ((TextView) view).getText();
        return text == null ? null : text.toString();
      }
      return null;
    }
  }

  class EditTextFilter implements Filter {

    @Override public boolean filter(View view) {
      return (view instanceof EditText);
      //            return (view instanceof TextView || view instanceof AppCompatTextView) && !(view instanceof Button)
      //                    && !(view instanceof EditText)
      //                    && !(view instanceof CheckedTextView)
      //                    && !(view instanceof DigitalClock)
      //                    && !(view instanceof Chronometer);
    }

    @Override public String getContent(View view) {
      if (view instanceof EditText) {
        CharSequence text = ((TextView) view).getText();
        return text == null ? null : text.toString();
      }
      return null;
    }
  }

  class WeChatCellTextViewFilter implements Filter {

    private static final String TAG = "WeChatCellTextViewFilter";
    Class staticTextViewClass;
    private Method getTextMethod;

    WeChatCellTextViewFilter(ClassLoader classLoader) {
      try {
        staticTextViewClass =
            classLoader.loadClass("com.tencent.mm.ui.widget.celltextview.CellTextView");
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }

    @Override public boolean filter(View view) {
      if (staticTextViewClass != null) {
        return staticTextViewClass.isInstance(view);
      }
      return false;
    }

    @Override public String getContent(View view) {
      if (!staticTextViewClass.isInstance(view)) {
        return null;
      }
      if (staticTextViewClass != null) {
        try {
          if (getTextMethod == null) {
            try {
              getTextMethod = staticTextViewClass.getMethod("cgv");
            } catch (NoSuchMethodException e) {
              e.printStackTrace();
              Method[] methods = staticTextViewClass.getDeclaredMethods();
              if (methods != null) {
                for (Method method : methods) {
                  if (method.getReturnType().equals(String.class)
                      && method.getParameterTypes().length == 0) {
                    getTextMethod = method;
                  }
                }
              }
            }
          }
          if (getTextMethod != null) {
            Object invoke = getTextMethod.invoke(view);
            if (invoke != null) {
              return invoke.toString();
            }
          }
        } catch (InvocationTargetException e) {
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
      return null;
    }
  }

  class WeChatValidFilter implements Filter {

    private static final String TAG = "WeChatValidFilter";
    Class staticTextViewClass;

    WeChatValidFilter(ClassLoader classLoader) {
      try {
        staticTextViewClass =
            classLoader.loadClass("com.tencent.mm.kiss.widget.textview.StaticTextView");
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }

    @Override public boolean filter(View view) {
      if (staticTextViewClass != null) {
        return staticTextViewClass.isInstance(view);
      }
      return false;
    }

    @Override public String getContent(View view) {
      if (!staticTextViewClass.isInstance(view)) {
        return null;
      }
      if (staticTextViewClass != null) {
        try {
          Method getText = staticTextViewClass.getMethod("getText");
          if (getText != null) {
            Object invoke = getText.invoke(view);
            if (invoke != null) {
              return invoke.toString();
            }
          }
        } catch (NoSuchMethodException e) {
          e.printStackTrace();
        } catch (InvocationTargetException e) {
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
      return null;
    }
  }

  class WeChatValidNoMeasuredTextViewFilter extends CommonViewFilter {

    WeChatValidNoMeasuredTextViewFilter(ClassLoader classLoader) {
      super(classLoader, "com.tencent.mm.ui.base.NoMeasuredTextView", "mText");
    }
  }

  class CommonViewFilter implements Filter {

    private static final String TAG = "CommonViewFilter";
    Class staticTextViewClass;
    private Field getTextField;
    String fieldName;
    String className;
    boolean hasLoadClassInFilter = false;

    CommonViewFilter(ClassLoader classLoader, String className, String fieldName) {
      this.fieldName = fieldName;
      this.className = className;
      try {
        staticTextViewClass = classLoader.loadClass(className);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }

    @Override public boolean filter(View view) {
      if (staticTextViewClass != null) {
        return staticTextViewClass.isInstance(view);
      } else if (!hasLoadClassInFilter) {
        hasLoadClassInFilter = true;
        try {
          staticTextViewClass = view.getClass().getClassLoader().loadClass(className);
          filter(view);
        } catch (ClassNotFoundException e) {
          e.printStackTrace();
        }
      }
      return false;
    }

    @Override public String getContent(View view) {
      if (!staticTextViewClass.isInstance(view)) {
        return null;
      }
      if (staticTextViewClass != null) {
        try {
          if (getTextField == null) {
            try {
              Field field = staticTextViewClass.getDeclaredField(fieldName);
              try {
                field.getType().asSubclass(CharSequence.class);
                getTextField = field;
                getTextField.setAccessible(true);
              } catch (Exception e) {
                e.printStackTrace();
                throw new NoSuchFieldException();
              }
            } catch (NoSuchFieldException e) {
              e.printStackTrace();
              Field[] fields = staticTextViewClass.getDeclaredFields();
              if (fields != null) {
                for (Field method : fields) {
                  try {
                    method.getType().asSubclass(CharSequence.class);
                    getTextField = method;
                    getTextField.setAccessible(true);
                    break;
                  } catch (Exception e1) {
                    e1.printStackTrace();
                  }
                }
              }
            }
          }
          if (getTextField != null) {
            Object invoke = getTextField.get(view);
            if (invoke != null) {
              return invoke.toString();
            }else {
              CharSequence desc= view.getContentDescription();
              if (desc!=null){
                return desc.toString();
              }
            }
          }
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
      return null;
    }
  }

  class QQSingleLineTextView extends CommonViewFilter {
    QQSingleLineTextView(ClassLoader classLoader) {
      super(classLoader, "com.tencent.widget.SingleLineTextView",
          "jdField_a_of_type_JavaLangCharSequence");
    }
  }

  class QQZoneFeedForwardTextView extends CommonViewFilter {
    QQZoneFeedForwardTextView(ClassLoader classLoader) {
      super(classLoader, "com.qzone.module.feedcomponent.ui.FeedForwardView", "a");
    }
  }

  class QQZonePraiseListView extends CommonViewFilter {
    QQZonePraiseListView(ClassLoader classLoader) {
      super(classLoader, "com.qzone.module.feedcomponent.ui.PraiseListView", "a");
    }
  }
}
