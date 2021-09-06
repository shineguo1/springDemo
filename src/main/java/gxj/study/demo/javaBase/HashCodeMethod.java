package gxj.study.demo.javaBase;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2021/9/6 10:16
 * @description
 */
public class HashCodeMethod {

    @Data
    @AllArgsConstructor
    static class Foo{
        String name;

        @Override
        public boolean equals(Object f){
            return true;
        }
    }

    @Data
    @AllArgsConstructor
    static class Foo2{
        String name;

        @Override
        public boolean equals(Object f){
            return f instanceof Foo2 && name.equals(((Foo2)f).getName());
        }

        @Override
        public int hashCode(){
            return 5 * 31 + name.hashCode();
        }
    }


    public static void main(String[] args) {
        //未改写hashCode，在加入hashSet时会被判断为key（hashCode）不等
        testFoo();
        testFoo2();
    }

    private static void testFoo2() {
        System.out.println("\nFoo2改写hashCode");
        Foo2 f1 = new Foo2("abc");
        Foo2 f2 = new Foo2("abc");
        HashSet<Foo2> set2 = Sets.newHashSet();
        set2.add(f1);
        set2.add(f2);
        System.out.println("f1 equals f2 : "+ f1.equals(f2));
        System.out.println("f1、f2 加入HashSet: "+ set2.toString());
    }

    private static void testFoo() {
        System.out.println("Foo未改写hashCode");
        Foo o1 = new Foo("abc");
        Foo o2 = new Foo("abc");
        HashSet<Foo> set1 = Sets.newHashSet();
        set1.add(o1);
        set1.add(o2);
        System.out.println("o1 equals o2 : "+ o1.equals(o2));
        System.out.println("o1、o2 加入HashSet: "+ set1.toString());
    }
}
