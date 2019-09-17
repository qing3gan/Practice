package datastruct.struct;

import org.apache.commons.lang3.ObjectUtils;

/**
 * 栈(FILO)
 */
public class LinkedStackADT<T> {

    static class Node<T> {
        T data;
        Node next;

        Node(T data) {
            this.data = data;
        }
    }

    private Node top = null;

    public void push(T value) {
        Node newNode = new Node<>(value);
        if (top == null) top = newNode;
        else {
            newNode.next = top;
            top = newNode;
        }
    }

    public T pop() {
        if (top == null) return null;
        T value = (T) top.data;
        top = top.next;
        return value;
    }

    public T pick() {
        if (top == null) return null;
        return (T) top.data;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public void printStack() {
        Node p = top;
        while (p != null) {
            System.out.print(p.data + " ");
            p = p.next;
        }
        System.out.println();
    }

    static class Operator {
        static int getPriority(Character operator) {
            switch (operator) {
                case '+':
                case '-':
                    return 1;
                case '*':
                case '/':
                    return 2;
            }
            return 0;
        }

        static int comparePriority(Character oprt1, Character oprt2) {
            if (ObjectUtils.equals(oprt1, oprt2)) {
                return 0;
            } else if (getPriority(oprt1) < getPriority(oprt2)) {
                return -1;
            } else {
                return 1;
            }
        }

        static Integer operate(Character operator, Integer operand1, Integer operand2) {
            switch (operator) {
                case '+':
                    return operand1 + operand2;
                case '-':
                    return operand1 - operand2;
                case '*':
                    return operand1 * operand2;
                case '/':
                    return operand1 / operand2;
            }
            return 0;
        }
    }

    static Integer calculate(String expression) {
        LinkedStackADT<Integer> operand = new LinkedStackADT<>();
        LinkedStackADT<Character> operator = new LinkedStackADT<>();
        for (Character exp : expression.toCharArray()) {
            if (Character.isDigit(exp)) {
                operand.push(Character.getNumericValue(exp));
            } else {
                Character operator1 = operator.pick();
                if (operator1 == null) operator.push(exp);
                else {
                    if (Operator.comparePriority(exp, operator1) > 0) {
                        operator.push(exp);
                    } else {
                        Integer operand1 = operand.pop();
                        Integer operand2 = operand.pop();
                        Integer result1 = Operator.operate(operator.pop(), operand1, operand2);
                        operand.push(result1);
                        while (!operator.isEmpty() && Operator.comparePriority(operator.pick(), exp) > 0) {
                            Integer operand4 = operand.pop();
                            Integer operand3 = operand.pop();
                            Integer result2 = Operator.operate(operator.pop(), operand3, operand4);
                            operand.push(result2);
                        }
                        operator.push(exp);
                    }
                }
            }
        }
        while (!operator.isEmpty()) {
            Integer operand2 = operand.pop();
            Integer operand1 = operand.pop();
            Integer result = Operator.operate(operator.pop(), operand1, operand2);
            operand.push(result);
        }
        return operand.pop();
    }

    public static void main(String[] args) {
//        LinkedStackADT stack = new LinkedStackADT();
//        for (int i = 1; i <= 10; i++) {
//            stack.push(i);
//        }
//        stack.printStack();
        System.out.println(calculate("1+2*3-4/4+5*6-7/7"));
    }
}
