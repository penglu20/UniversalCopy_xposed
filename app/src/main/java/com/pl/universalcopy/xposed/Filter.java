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

        @Override
        public boolean filter(View view) {
            return (view instanceof TextView )&& !(view instanceof EditText);
//            return (view instanceof TextView || view instanceof AppCompatTextView) && !(view instanceof Button)
//                    && !(view instanceof EditText)
//                    && !(view instanceof CheckedTextView)
//                    && !(view instanceof DigitalClock)
//                    && !(view instanceof Chronometer);
        }

        @Override
        public String getContent(View view) {
            if (view instanceof TextView) {
                CharSequence text = ((TextView) view).getText();
                return text==null?null:text.toString();
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
                staticTextViewClass = classLoader.loadClass("com.tencent.mm.kiss.widget.textview.StaticTextView");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean filter(View view) {
            if (staticTextViewClass != null) {
                return staticTextViewClass.isInstance(view);
            }
            return false;
        }

        @Override
        public String getContent(View view) {
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
    class WeChatValidNoMeasuredTextViewFilter implements Filter {

        private static final String TAG = "WeChatValidNoMeasuredTextViewFilter";
        Class staticTextViewClass;
        private Field getTextField;

        WeChatValidNoMeasuredTextViewFilter(ClassLoader classLoader) {
            try {
                staticTextViewClass = classLoader.loadClass("com.tencent.mm.ui.base.NoMeasuredTextView");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean filter(View view) {
            if (staticTextViewClass != null) {
                return staticTextViewClass.isInstance(view);
            }
            return false;
        }

        @Override
        public String getContent(View view) {
            if (!staticTextViewClass.isInstance(view)) {
                return null;
            }
            if (staticTextViewClass != null) {
                try {
                    try {
                        getTextField = staticTextViewClass.getDeclaredField("mText");
                        getTextField.setAccessible(true);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                        Field[] fields = staticTextViewClass.getDeclaredFields();
                        if (fields != null) {
                            for (Field method : fields) {
                                if (method.getType().isInstance(CharSequence.class)) {
                                    getTextField = method;
                                    getTextField.setAccessible(true);
                                }
                            }
                        }
                    }
                    if (getTextField != null) {
                        Object invoke = getTextField.get(view);
                        if (invoke != null) {
                            return invoke.toString();
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
