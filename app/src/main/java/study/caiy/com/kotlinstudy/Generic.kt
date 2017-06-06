package study.caiy.com.kotlinstudy

import android.util.Log

/**
 * Created by yongc on 2017/6/6.
 */

fun random(one: Any, two:Any, three: Any): Any{//普通函数
    return one;
}

fun <T> randomGeneric(one: T, two: T, three: T): T{//泛型参数函数
    return one;
}

fun <K,V> put4Study(key: K, value: V):Unit{//多个泛型参数函数
    Log.i(TAG,"key=$key,value=$value")
}

fun studyGeneric(){
    studyParameterisedFunction()
    studyParameterisedType()
    studyUpperBounds()
}

fun studyParameterisedFunction(){
    Log.i(TAG,"---studyParameterisedFunction start---")

    val r:Any = random("a","b","c");//缺点：使用变量r的时候需要自己判断r的具体类型进行转换，容易出错并且代码不简洁
    Log.i(TAG,"r=$r")
//    val r1:String = random("a","b","c");//无法编译通过

    var r2:String = randomGeneric("a","b","c");//类型推断出是String类型
    Log.i(TAG,"r2=$r2")
    var r3:Int = randomGeneric(1,2,3);//类型推断出是Int类型
    Log.i(TAG,"r3=$r3")

    var r4:Any = randomGeneric("a",1,true);//向上类型推断出是Any类型(Any是所有类型的基类 )
    Log.i(TAG,"r4=$r4")

    put4Study(1,"a");//多个类型参数示例

    Log.i(TAG,"---studyParameterisedFunction end---")
}


class Sequence<T>{//一个类型参数的类

}

class Dictionary<K,V>{//两个类型参数的类

}


fun studyParameterisedType() {
    Log.i(TAG, "---studyParameterisedType start---")

    val seqBoolean = Sequence<Boolean>()//实例化
    val seqString = Sequence<String>()//实例化

    val dict = Dictionary<Int,String>();//实例化

    Log.i(TAG, "---studyParameterisedType end---")
}

/**
 * 未使用上界的泛型话
 * 无法比较first和second的大小
 */
fun <T> min(first: T,second: T):T{
    return first;//无法比较first和second的大小
}

/**
 * 存在上界的泛型
 * 可以接收所有Comparable的子类
 */
fun <T : Comparable<T>> minUpperBounds(first: T,second: T):T{
    val result = first.compareTo(second);
    if(result <=0){
        return first;
    }else{
        return second;
    }
}

fun studyUpperBounds() {
    Log.i(TAG, "---studyUpperBounds start---")

    val a:Int = minUpperBounds(3,6);//Int实现了Comparable接口
    Log.i(TAG, "minUpperBounds(3,6)=$a")

    val b:String = minUpperBounds("b","c");//String实现了Comparable接口
    Log.i(TAG, "minUpperBounds(\"b\",\"c\")=$b")

    val c: Any = min("c",1);//普通泛型函数
//    val d: Any = minUpperBounds("d",1);//Any未实现Comparable接口，编译错误，参数必须是同类型的


    Log.i(TAG, "---studyUpperBounds end---")
}