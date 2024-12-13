package org.example;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Test1 {

    private int a=0;

    public int getA(){
        return a;
    }

    public Test1(){

    }

    public void setA(int a){
        this.a = a;
    }

    public static void test(Test1 t){
        t = new Test1();
        t.setA(100);
    }


    public static void main(String[] args) {
//        Test1 t=null;
//
//        test(t);
//        System.out.println(t.getA());

        List<Integer> list= Arrays.asList(1,2,3);
        List<String> list2= Arrays.asList("a","b","c");
        List list1 = list;
        for (int i = 0; i < list1.size(); i++){
            System.out.println(list1.get(i));
        }
        list1 = list2;
        for (int i = 0; i < list1.size(); i++){
            System.out.println(list1.get(i));
        }

    }


}
