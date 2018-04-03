package com.tsystems.javaschool.tasks.calculator;


import java.util.*;

public class Calculator {

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */

    private static final char[] nums = {'0','1','2','3','4','5','6','7','8','9'};
    private static final char[] symbols = {'+','-','*','/','(',')'};

    public String evaluate(String statement) {
        Queue queue = sortedStationAlgo(statement);
        if (queue == null) return null;
        Float res = countReversedPolishNotation(queue);
        if (res==null) return null;
        if (isIntegerWithZeroes(res)) return ((Integer)Math.round(res)).toString();
        else return trimToDigits(res);
    }

    private Float countReversedPolishNotation(Queue queue){
        if (queue==null) return null;
        Stack<Object> stack = new Stack();
        while (!queue.isEmpty()){
            Object obj = queue.remove();
            if ((obj instanceof Integer)||(obj instanceof Float)) stack.push(obj);
            if (obj instanceof Character){
                char operation = (char)obj;
                Float num1, num2;
                if (stack.peek() instanceof Float) num1 = (Float) stack.pop();
                else num1 = Float.valueOf((Integer) stack.pop());
                if (stack.peek() instanceof Float) num2 = (Float) stack.pop();
                else num2 = Float.valueOf((Integer) stack.pop());
                Float result ;
                switch (operation){
                    case '+' : result = num1+num2; break;
                    case '-' : result = num2-num1; break;
                    case '*' : result = num1*num2; break;
                    default: result = null;
                }
                if (operation == '/'){
                    if (num1!=0) result = num2/num1;
                    else return null;
                }
                stack.push(result);
            }
        }
        return (Float)stack.pop();
    }

    private String trimToDigits(Float f){
        String[] strings = f.toString().split("\\.");
        char[] chars = strings[1].toCharArray();
        char[] chars2 = new char[4];
        if ((chars[4] - '0') >=5){
            chars[3] += 1;
        }
        for(int i = 0; i < chars2.length; i++){
            chars2[i] = chars[i];
        }
        int zeroSequenceCounter = 0;
        for(int i = chars2.length-1; i>=0; i--){
            if (chars2[i] == '0') zeroSequenceCounter++;
            else break;
        }
        StringBuilder sb = new StringBuilder("");
        for(int i = 0; i < chars2.length-zeroSequenceCounter;i++){
            sb.append(chars2[i]);
        }
        return strings[0].concat(".").concat(sb.toString());
    }

    private boolean isNum(char c){
        for(int i =0; i < nums.length; i++){
            if (c == nums[i]) return true;
        }
        return false;
    }

    private boolean isSymbol(char c){
        for(int i =0; i < symbols.length; i++){
            if (c == symbols[i]) return true;
        }
        return false;
    }


    private String findNumInString(String string){
        StringBuilder sb = new StringBuilder("");
        char[] chars = string.toCharArray();
        //boolean dotFound = false;
        for(int i = 0; i < chars.length; i++){
            //if (chars[i] == '.') dotFound = true;
            if (!isSymbol(chars[i])) sb.append(chars[i]);
            else break;
        }
        return sb.toString();
    }

    private boolean isIntegerWithZeroes(Float f){
        if (f - Math.round(f) == 0){
            return true;
        }
        return false;
    }

    private boolean isStringValid(String string){
        // check if string has valid format
        if (string == null) return false;
        int openBracketCounter = 0, closeBracketCounter = 0;
        boolean openBracketFoundFirst = false, closeBracketFoundFirst = false;
        boolean atLeastOneNumFound = false, atLeastOneOperationSignFound = false;
        char[] chars = string.toCharArray();
        for(int i = 0; i < chars.length; i++){
            if ((!isNum(chars[i]) && (!isSymbol(chars[i])) && (chars[i]!='.'))) return false;
            if (isNum(chars[i])) atLeastOneNumFound = true;
            if (chars[i] == '+'  || chars[i] == '*' || chars[i] == '/') atLeastOneOperationSignFound = true;
            if ((chars[i] == '-' ) && (i != 0 && i < chars.length - 1) && ((chars[i - 1] == ')' || isNum(chars[i - 1])) && (chars[i + 1] == '(' || isNum(chars[i + 1]))))
                    atLeastOneOperationSignFound = true;
            if (i == 0 && (chars[i] == ')' || chars[i]=='.' || chars[i] == '+' || chars[i] == '*' || chars[i] == '/')) return false;
            if (i==chars.length-1 && (chars[i] == '(' || chars[i] == '.' || chars[i] == '+' || chars[i] == '-' || chars[i] == '*' || chars[i] == '/' )) return false;
            if (chars[i] == '(') {
                openBracketCounter++;
                if (!closeBracketFoundFirst) openBracketFoundFirst = true;
                if ((i!=0 && i < chars.length-1) && (chars[i + 1] == ')' || chars[i + 1] == '.' || chars[i + 1] == '+' || chars[i + 1] == '*' || chars[i + 1] == '/')) return false;
            }
            if (chars[i] == ')') {
                closeBracketCounter++;
                if (!openBracketFoundFirst)  return false;
            }
            if ((chars[i] == '.' && i!=0 && i < chars.length-1) && (chars[i + 1] == '.' || chars[i + 1] == ')' || chars[i + 1] == '(')) return false;
            if (( chars[i] == '+' || chars[i] == '-' || chars[i] == '*' || chars[i] == '/') && (i != 0 && i < chars.length - 1)
                    && (chars[i + 1] == '+' || chars[i + 1] == '-' || chars[i + 1] == '*' || chars[i + 1] == '/' || chars[i + 1] == ')')) return false;
        }
        if (openBracketCounter!=closeBracketCounter || !atLeastOneNumFound || !atLeastOneOperationSignFound) return false;
        return true;
    }

    private int addNumFromStringToList(String string, List<Object> where, boolean negative){
        String sNum = findNumInString(string);
        Float f = Float.parseFloat(sNum);
        if (negative) f = -f;
        if (isIntegerWithZeroes(f)){
            Integer integer = Math.round(f);
            where.add(integer);
        }
        else where.add(f);
         return sNum.length();
    }

    private List<Object> getTokens(String string) {
        if (!isStringValid(string)) return null;
        char[] chars = string.toCharArray();
        List<Object> list = new LinkedList<>();
        int i=0;
        while (i< chars.length){
            boolean processed = false;
            if ((chars[i] == '-' && i==0) || (chars[i] == '-' && i!=0 && chars[i-1]=='(')){
                i += addNumFromStringToList(string.substring(i+1), list, true);
                processed = true;
            }
            if (isNum(chars[i])){
                i += addNumFromStringToList(string.substring(i), list, false);
                processed = true;
            }
            if ( i < chars.length && isSymbol(chars[i]) && !processed){
                list.add(chars[i]);
                i++;
            }
        }
        return list;
    }

    private void executeSortedStationActionIfOperator(char c, Queue<Object> queue, Stack<Object> stack){
        if (stack.empty()) stack.push(c);
        else {
            Object obj = null;
            while ((!stack.empty()) && ((obj = stack.peek()) != null)) {
                char op2 = (char) stack.peek();
                if     ( ( (c == '+') && (op2 != '(')) || ( (c == '-') && (op2 != '(')) || ( c == '*' && (op2 == '*' || op2 == '/')) || ( c == '/' && (op2 == '*' || op2 == '/'))) {
                    stack.pop();
                    queue.offer(obj);
                    if (stack.empty()){
                        stack.push(c);
                        break;
                    }
                }
                else {
                    stack.push(c);
                    break;
                }
            }
        }
    }

    private Queue<Object> sortedStationAlgo(String string){
        List<Object> tokens = getTokens(string);
        if (tokens == null) return null;
        Queue<Object> queue = new LinkedList<Object>();
        Stack<Object> stack = new Stack<>();
        for(Object o:tokens){
            if ( o instanceof Float || o instanceof Integer) queue.offer(o);
            if (o instanceof Character){
                char c = (char)o;
                if ((c == '+') || (c == '-') || (c == '*') || (c == '/')){
                    executeSortedStationActionIfOperator(c,queue,stack);
                }
                if (c == '('){
                    stack.push(c);
                }
                if (c == ')'){
                    Object obj = null;
                    while ((!stack.empty()) && (( ((char)(obj = stack.pop())) != '('))){
                        queue.offer(obj);
                    }
                }
            }
        }
        Object obj;
        if (!stack.empty()) while ((!stack.empty()) && (((obj = stack.pop()) != null))) queue.offer(obj);
        return queue;
    }
}
