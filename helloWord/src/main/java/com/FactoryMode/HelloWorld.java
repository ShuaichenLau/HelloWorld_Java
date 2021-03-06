package com.FactoryMode;

import java.util.UUID;

/**
 * 工厂设计模式
 *  实现了创建者和调用者分离,工厂模式分为简单工厂,工厂方法,抽象工厂模式
 * 工厂模式的好处
 *  工程模式是我们最常用的实例化对象模式了,是用工厂方法代理new操作的一种模式.
 *  利用工厂模式可以降低程序的耦合性,为后期的维护修改提供了很大的便利.
 *  将选择实现类,创建对象统一管理和控制.从而将调用者跟我们的实现类解耦.
 *
 * 简单工厂模式
 *  简单工厂模式相当于是一个工厂中有各种产品,创建在一个类中,客户无需知道具体产品的名称,只需要知道产品类所对应的参数即可.但是工厂的职责过重,而且当类型过多时不利于系统的扩展维护.
 *
 *
 *
 */
public class HelloWorld {

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString().replaceAll("-",""));
    }
}
