package study.caiy.com.kotlinstudy

import android.util.Log
import java.io.Serializable
import java.math.BigDecimal
import java.util.*

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
    studyMultiBounds()
    studyInvariance()
    studyCovariant()
    studyCovariantReturn()
    studyContravariance()
    studyNothing()
    studyTypeProjection()
    studyTypeErasure()
    studyTypeReification()
    studyRecursiveTypeBounds()
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

/**
 * 函数 泛型参数 多个上界
 * 关键字where
 */
fun<T> minSerializable(first: T, second: T) : T
where T : Comparable<T>, T : Serializable{
    val result = first.compareTo(second);
    if(result <=0){
        return first;
    }else{
        return second;
    }
}

/**
 * 只实现了Comparable<Year>接口
 */
class Year(val value: Int) : Comparable<Year>{
    override fun compareTo(other: Year): Int {
        return this.value.compareTo(other.value)
    }
}

/**
 * 实现了Comparable<SerializableYear>和Serializable两个接口
 */
class SerializableYear(val value:Int): Comparable<SerializableYear>,Serializable{
    override fun compareTo(other: SerializableYear): Int {
        return this.value.compareTo(other.value)
    }
}

/**
 * 类 泛型参数 多个上界
 * 关键字 where T:Comparable<T>,T:Serializable
 */
class MultiBoundClass<T>(val value:T) where T:Comparable<T>,T:Serializable{

    fun customCompareTo(other:MultiBoundClass<T>):Int{
        return value.compareTo(other.value);
    }

}

fun studyMultiBounds(){
    Log.i(TAG, "---studyMultiBounds start---")

    //函数的 有多个上界的泛型参数
//    minSerializable(Year(2017),Year(2018))//编译错误，Year不是Serializable的子类
    val minYear = minSerializable(SerializableYear(2017),SerializableYear(2018))
    Log.i(TAG,"minSerializable(SerializableYear(2017),SerializableYear(2018))结果是${minYear.value}")

//    MultiBoundClass(Year(2017));//编译错误，Year不是Serializable的子类
    val multiBound2017 = MultiBoundClass(SerializableYear(2017))
    val multiBound2018 = MultiBoundClass(SerializableYear(2018))
    val result = multiBound2017.customCompareTo(multiBound2018)
    Log.i(TAG,"multiBound2017.customCompareTo(multiBound2018)结果是$result")

    Log.i(TAG, "---studyMultiBounds end---")
}

open class Fruit{

}

class Apple:Fruit(){

}

class Orange:Fruit(){

}

class Crate<T>(val elements:MutableList<T>){
    fun add(t: T){
        elements.add(t)
    }

    fun last(): T{
        return elements.last()
    }

}

fun foo(crate: Crate<Fruit>){
    crate.add(Apple())
}

fun studyInvariance(){
    Log.i(TAG, "---studyInvariance start---")

    var oranges = Crate(mutableListOf(Orange(), Orange()))
//    foo(oranges)//编译错误,Crate<Orange>不是Crate<Fruit>的子类型
    var fruits = Crate(mutableListOf(Fruit(), Fruit()))

    Log.i(TAG, "---studyInvariance end---")
}

/**
 * 协变
 * 关键词out
 */
class CovariantCrate<out T>(val elements: List<T>){
    //编译错误 T声明成了out，可以理解成生成者。对于我们只能作为返回值，不能作为入参
//    fun add(t: T){
//        elements.add(t)
//    }

    fun last(): T{
        return elements.last()
    }
}

fun fooCovariant(crate: CovariantCrate<Fruit>){
    //do nothing
}

fun studyCovariant(){
    Log.i(TAG, "---studyCovariant start---")

    val covariantOranges:CovariantCrate<Orange> = CovariantCrate(listOf(Orange(), Orange()))
    fooCovariant(covariantOranges);//因为关键字out,CovariantCrate<Orange>被当做了CovariantCrate<Fruit>的子类

    Log.i(TAG, "---studyCovariant end---")
}

open class Animal{

}

class Sheep : Animal(){
    fun onlyInSheep(){

    }
}

open class Farm {
    open fun get(): Animal{
        return Animal()
    }
}
class SheepFarm() : Farm() {
    override fun get(): Sheep{//函数返回值 型变 Sheep是Animal的子类
        return Sheep()
    }
}

/**
 * 型变返回
 * TODO--- 感觉和泛型没有关系
 */
fun studyCovariantReturn(){
    Log.i(TAG, "---studyCovariantReturn start---")

    val farm: Farm = SheepFarm()//显示声明为Farm类型
    val animal1 = farm.get()
//    animal1.onlyInSheep();//无法编译通过，因为farm.get()得到的是一个Animal类型，没有onlyInSheep方法

    val sheepFarm = SheepFarm()//默认为SheepFarm类型
    val animal2 = sheepFarm.get()
    animal2.onlyInSheep();

    Log.i(TAG, "---studyCovariantReturn end---")
}

interface Listener<T> {
    fun onNext(t: T): Unit
}
class EventStream<T>(val listener: Listener<T>) {
    fun start(t: T): Unit{
        listener.onNext(t);
    }
    fun stop(): Unit{

    }
}

/**
 * 逆型变 关键词 in 消费者 只能作为函数入参，不能作为返回值
 */
interface Listener4Contra<in T> {
    fun onNext(t: T): Unit
}
class EventStream4Contra<in T>(val listener: Listener4Contra<T>) {
    fun start(t: T): Unit{
        listener.onNext(t);
    }
    fun stop(): Unit{

    }
}

fun studyContravariance(){
    Log.i(TAG, "---studyContravariance start---")

    //非型变示例
    val stringListener = object : Listener<String> {
        override fun onNext(t: String){
            Log.i(TAG,t)
        }
    }
    val stringStream = EventStream<String>(stringListener)//stringListener是Listener<String>类型，Listener<T>的子类一
    stringStream.start("a")
    val dateListener = object : Listener<Date> {
        override fun onNext(t: Date){
            Log.i(TAG,t.toString())
        }
    }
    val dateStream = EventStream<Date>(dateListener)//dateListener是Listener<Date>类型，Listener<T>的子类二
    dateStream.start(Date())

    //可以看到这里定义了两个listener:stringListener、dateListener
    //逆型变（关键字in）可以只用一个listener实现功能

    val loggingListener = object :
    Listener4Contra<Any> {
        override fun onNext(t: Any){
            Log.i(TAG,t.toString())
        }
    }
    EventStream4Contra<String>(loggingListener).start("b")//String是Any的子类
    EventStream4Contra<Date>(loggingListener).start(Date())//Date是Any的子类

    Log.i(TAG, "---studyContravariance end---")
}

class Box<out T>(){
}

interface Marshaller<out T> {
    fun marshall(json: String): T?//函数返回参数类型为T
}

object StringMarshaller : Marshaller<String>{
    override fun marshall(json: String): String? {//函数返回参数类型为String
        return json;
    }
}

object NoopMarshaller : Marshaller<Nothing> {
    override fun marshall(json: String) = null////函数返回参数空
}

/**
 * 关键字Nothing
 * TODO 使用意义不太了解
 * 如果一个函数不需要返回值，可以使用Nothing（官方文档上说：一个函数如果是死循环，除非线程被杀死，或者抛出异常，否则不会返回的方法）
 */
fun studyNothing(){
    Log.i(TAG, "---studyNothing start---")

    Box<String>()//正常实例化
    Box<Nothing>()//Nothing实例化

    val a = StringMarshaller.marshall("a")
    Log.i(TAG,"a=$a")
    val b = NoopMarshaller.marshall("b")
    Log.i(TAG,"b=$b")
    Log.i(TAG, "---studyNothing end---")
}

fun getWithCrate(crate: Crate<Fruit>){
    //do nothing
}

fun getWithCrateWhenProjection(crate: Crate<out Fruit>){
//    crate.add(Fruit())//编译不通过，因为是out类型，add方法被禁止访问
    Log.i(TAG,"crate.last()结果是：" + crate.last())
}

class EventStream4Projection<T>(val listener: Listener<in T>) {
    fun start(t: T): Unit{
        listener.onNext(t);
    }
    fun stop(): Unit{

    }
}

/**
 * 泛型投影
 *
 */
fun studyTypeProjection(){
    Log.i(TAG, "---studyTypeProjection start---")

    val oranges = Crate(mutableListOf(Orange(),Orange()))
//    getWithCrate(oranges)//编译错误，Crate<Orange>不能转换为Crate<Fruit>
    //假定Crate是第三方提供的类，不可修改。
    //方法中加入关键字out:Crate<Orange>类型投影成了Crate<Fruit>
    getWithCrateWhenProjection(oranges)

    val loggingListener = object :Listener<Any> {
        override fun onNext(t: Any){
            Log.i(TAG,t.toString())
        }
    }
//    EventStream<String>(loggingListener)//无法编译通过，需要Listener<String>,实际传递Listener<Any>
    //假定Listener是第三方提供的接口，无法修改
    //loggingListener一个listener适用于两个
    EventStream4Projection<String>(loggingListener).start("b")//方法中加入关键字in : Listener<Any>类型投影成了Listener<String>
    EventStream4Projection<Date>(loggingListener).start(Date())//方法中加入关键字in : Listener<Any>类型投影成了Listener<Date>

    Log.i(TAG, "---studyTypeProjection end---")
}

fun printInts(list: Set<Int>): Unit {
    for (int in list) Log.i(TAG,int.toString())
}
fun printStrings(list: Set<String>): Unit {
    for (string in list) Log.i(TAG,string)
}

fun <T : Comparable<T>>max(list: List<T>): T {
    var max = list.first()
    for (t in list) {
        if (t >max)
            max = t
    }
    return max
}

/**
 * 类型擦除
 */
fun studyTypeErasure(){
    Log.i(TAG, "---studyTypeErasure start---")
    printInts(setOf(1,2))
    printStrings(setOf("a","b"))
    Log.i(TAG,max(listOf("c","d")));
    Log.i(TAG, "---studyTypeErasure end---")
}

inline fun <T>runtimeTypeNormal(): Unit {
//    println("My type parameter is " + T::class.qualifiedName)//编译错误，无法调用::class
}

/**
 *
 * 运行时获取T的实际类型
 * reified关键字，必须和inline配合使用
 */
inline fun <reified T>runtimeType4Refication(): Unit {
    println("My type parameter is " + T::class.qualifiedName)
}

/**
 * 运行时类型检查
 */
inline fun <reified T>List<Any>.collect(): List<T> {
    return this.filter { it is T }.map { it as T }
}

/**
 * 运行时类型检查 示例2
 */
inline fun <reified T>printT(any: Any): Unit {
    if (any is T) {
        Log.i(TAG,"I am a T: $any")
    }else{
        Log.i(TAG,"not match")
    }
}

/**
 * 类型具化
 */
fun studyTypeReification(){
    Log.i(TAG, "---studyTypeReification start---")

    //运行时获取T的实际类型
    runtimeType4Refication<String>();
    runtimeType4Refication<Int>();

    //运行时类型检查
    val list = listOf("green", false, 100, "blue")
    val strings = list.collect<String>()
    Log.i(TAG,"strings=$strings")

    //字节码示例
    printT<String>("a");
    printT<String>(1);

    Log.i(TAG, "---studyTypeReification end---")
}

interface Account {
    val balance: BigDecimal
}
data class SavingsAccount(override val balance: BigDecimal,val interestRate: BigDecimal) : Account,Comparable<SavingsAccount> {
    override fun compareTo(other: SavingsAccount): Int =
            balance.compareTo(other.balance)
}
data class TradingAccount(override val balance: BigDecimal, val margin:Boolean) : Account, Comparable<TradingAccount> {
    override fun compareTo(other: TradingAccount): Int =
            balance.compareTo(other.balance)
}

interface Account2 : Comparable<Account2> {
    val balance: BigDecimal
    override fun compareTo(other: Account2): Int =
            balance.compareTo(other.balance)
}
data class SavingsAccount2(override val balance: BigDecimal,val interestRate: BigDecimal) : Account2{
}
data class TradingAccount2(override val balance: BigDecimal, val margin:Boolean) : Account2{
}

interface Account3<E> : Comparable<E> {
    val balance: BigDecimal
//    override fun compareTo(other: E): Int = balance.compareTo(other.balance)//编译错误，编译期无法判断泛型参数other有balance属性
}
//data class SavingsAccount3(override val balance: BigDecimal, val interestRate: BigDecimal) : Account3<SavingsAccount3>
//data class TradingAccount3(override val balance: BigDecimal, val margin:Boolean) : Account3<TradingAccount3>

interface Account4<E : Account4<E>> : Comparable<E> {
    val balance: BigDecimal
    override fun compareTo(other: E): Int = balance.compareTo(other.balance)
}
data class SavingsAccount4(override val balance: BigDecimal, val interestRate: BigDecimal) : Account4<SavingsAccount4>
data class TradingAccount4(override val balance: BigDecimal, val margin:Boolean) : Account4<TradingAccount4>

/**
 * 缺陷：BettingAccount和泛型参数SavingsAccount4可以不同
 */
abstract class BettingAccount : Account4<SavingsAccount4>

/**
 * 最佳实践：BettingAccount4Best和泛型参数BettingAccount4Best相同
 */
abstract class BettingAccount4Best : Account4<BettingAccount4Best>

/**
 * 嵌套泛型边界
 * 举例 <E : Account4<E>>
 */
fun studyRecursiveTypeBounds(){
    Log.i(TAG, "---studyRecursiveTypeBounds start---")

    //方式一
    //缺点：compareTo在每个子类中都实现了，代码重复
    val savings1 = SavingsAccount(BigDecimal(105), BigDecimal(0.04))
    val savings2 = SavingsAccount(BigDecimal(396), BigDecimal(0.05))
    savings1.compareTo(savings2)
    val trading1 = TradingAccount(BigDecimal(211), true)
    val trading2 = TradingAccount(BigDecimal(853), false)
    trading1.compareTo(trading2)
//    savings1.compareTo(trading1)//编译错误

    //方式二
    //缺点：savings和trading是不同的子类型，（需求是不比较不同子类型的acount）
    val savings = SavingsAccount2(BigDecimal(105), BigDecimal(0.04))
    val trading = TradingAccount2(BigDecimal(210), true)
    savings.compareTo(trading)

    //方式三
    //无法编译成功 Account3及其子类

    //方式四
    val saving4s1 = SavingsAccount4(BigDecimal(105), BigDecimal(0.04))
    val saving4s2 = SavingsAccount4(BigDecimal(396), BigDecimal(0.05))
    val result1 = saving4s1.compareTo(saving4s2)
    Log.i(TAG,"result1=$result1")
    val trading41 = TradingAccount4(BigDecimal(853), true)
    val trading42 = TradingAccount4(BigDecimal(518), false)
    val result2 = trading41.compareTo(trading42)
    Log.i(TAG,"result2=$result2")
//    saving4s1.compareTo(trading42)//编译错误 达到目标

    //注意 BettingAccount4Best和BettingAccount类的区别

    Log.i(TAG, "---studyRecursiveTypeBounds end---")
}
