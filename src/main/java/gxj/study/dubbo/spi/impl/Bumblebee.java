package gxj.study.dubbo.spi.impl;

import gxj.study.dubbo.spi.Robot;
import gxj.study.dubbo.spi.Weapon;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/7/13 14:50
 * @description
 */
public class Bumblebee implements Robot {

    private Weapon weapon;

    @Override
    public void sayHello() {
        System.out.println("Hello, I am Bumblebee.");
    }

    public void setGun(Weapon weapon) {
        this.weapon = weapon;
    }

    public void fire(){
        this.weapon.sayHello();
    }
}