package com.example.zj.mvpdemo.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import com.example.zj.mvpdemo.base.AppApplication;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Limige on 2016/11/29.
 * 字符串处理
 */

public class CheckUtil
{

    /* 检查是否包含汉字 */
    public static boolean isContainChinese(String str)
    {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find())
        {
            return true;
        }
        return false;
    }

    /*判断字符串是否是整数*/
    public static boolean isInteger(String value)
    {
        try
        {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e)
        {
            return false;
        }
    }

    public static boolean isDouble(String value)
    {
        try
        {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e)
        {
            return false;
        }
    }

    /*过滤所有空格*/
    public static String clearBlank(String s)
    {
        if (s != null)
            s = s.replaceAll(" ", "").trim();
        return s;
    }

    /**
     * 判断APP是否启动
     */
    public static boolean isAppAlive()
    {
        if (AppApplication.CONTEXT == null || isEmpty(AppApplication.activities))
        {
            return false;
        }

        ActivityManager activityManager = (ActivityManager) AppApplication.CONTEXT
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = AppApplication.CONTEXT.getPackageName();
        /**
         * 获取Android设备中所有正在运行的App
         */
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
        {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses)
        {
            if (appProcess.processName.equals(packageName))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断APP是否前台运行
     */
    public static boolean isOnForeignGround()
    {
        if (AppApplication.CONTEXT == null || isEmpty(AppApplication.activities))
        {
            return false;
        }

        ActivityManager activityManager = (ActivityManager) AppApplication.CONTEXT
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = AppApplication.CONTEXT.getPackageName();
        /**
         * 获取Android设备中所有正在运行的App
         */
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
        {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses)
        {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND//判断APP是否前台运行
                    )
            {
                return true;
            }
        }

        return false;
    }

    /* 禁止EditText输入空格*/
    public static void setEditTextInhibitInputSpace(EditText editText)
    {
        InputFilter filter = new InputFilter()
        {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
            {
                if (source.equals(" "))
                {
                    return "";
                } else
                {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /* 禁止EditText输入特殊字符*/
    public static void setEditTextInhibitInputSpeChat(EditText editText)
    {
        InputFilter filter = new InputFilter()
        {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
            {
                String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) return "";
                else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /* 将String型数字字符串保留两位小数 */
    public static String round(String number)
    {
        if (isEmpty(number))
        {
            return "0.00";
        }
        try
        {
            BigDecimal b = new BigDecimal(number);
            BigDecimal one = new BigDecimal("1");
            number = String.format("%.2f", b.divide(one, 2, BigDecimal.ROUND_HALF_UP).doubleValue());
        } catch (Exception e)
        {
        }
        return number;
    }

    /* 将String型数字字符串保留1位小数 */
    public static String roundByOnlyOne(String number)
    {
        if (isEmpty(number))
        {
            return "0.0";
        }
        try
        {
            BigDecimal b = new BigDecimal(number);
            BigDecimal one = new BigDecimal("1");
            number = String.format("%.1f", b.divide(one, 2, BigDecimal.ROUND_HALF_UP).doubleValue());
        } catch (Exception e)
        {
        }
        return number;
    }

    /* 去.0 */
    public static String deletePointZero(String number)
    {
        if (!isEmpty(number) && number.endsWith(".0"))
        {
            return number.substring(0, number.length() - 2);
        }
        return number;
    }

    /* 检查是否为空 */
    public static boolean isEmpty(String value)
    {
        return value == null || value.trim().length() == 0 || "".equals(value.trim()) || "null".equals(value.trim());
    }

    public static boolean isEmpty(List list)
    {
        return list == null || list.size() <= 0;
    }

    /**
     * 判断数字是否是 空 或者 0
     *
     * @param num
     * @return
     */
    public static boolean isEmptyNum(String num)
    {
        return isEmpty(num) || num.startsWith("0");
    }

    /**
     * 判断str1中包含str2的个数
     *
     * @param str1
     * @param str2
     * @return counter
     */
    public static int counter = 0;

    public static int countStr(String str1, String str2)
    {
        if (str1.indexOf(str2) == -1)
        {
            return 0;
        } else if (str1.indexOf(str2) != -1)
        {
            counter++;
            countStr(str1.substring(str1.indexOf(str2) +
                    str2.length()), str2);
            return counter;
        }
        return 0;
    }
    /**
     * 判断一个字符串是数字和字母的结合
     * @param pass
     * @return
     */
    /**
     * 检查密码是否合法
     */
    public static boolean checkPassword(String password)
    {
        boolean flag = false;
        Pattern p = Pattern
                .compile(".*[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]");
        Matcher match = p.matcher(password);
        if (password != null && password.length() >= 6 && password.length() <= 20)
        {
            flag = match.matches();
        }
        return flag;
    }

    /**
     * 只允许汉字
     */
    public static boolean isMandarin(String str)
    {
        String regEx = "^[\u4e00-\u9fa5]+((·|•|·)[\u4e00-\u9fa5]+)*$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 邮箱的合法性
     *
     * @param email 邮箱
     * @return true 合法 false 不合法
     */
    public static boolean checkEmail(String email)
    {
        String reg = "\\w+(\\.\\w+)*@\\w+(\\.\\w+)+";
        Pattern pat = Pattern.compile(reg);
        Matcher mat = pat.matcher(email);
        return mat.matches();
    }

    /**
     * 检查手机号
     *
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone)
    {

        String reg = "(^(\\d{11,12})$)";

        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(phone);

        return m.matches();
    }

}
