package study.caiy.com.kotlinstudy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

val TAG = "TAG";

fun sum(a:Int,b:Int):Int{
    return a+b;
}

val ints = intArrayOf(1,2,0,3,4,5)

fun foo() {
    ints.forEach(fun(value: Int) {
        if (value == 0) return
        Log.i(TAG,value.toString())
    })
}

open class D {
}

class D1 : D() {
}

open class C {
    open fun D.foo() {
        Log.i(TAG,"D.foo in C")
    }

    open fun D1.foo() {
        Log.i(TAG,"D1.foo in C")
    }

    fun caller(d: D) {
        d.foo()   // 调用扩展函数
    }
}

class C1 : C() {
    override fun D.foo() {
        Log.i(TAG,"D.foo in C1")
    }

    override fun D1.foo() {
        Log.i(TAG,"D1.foo in C1")
    }
}

class MainActivity : AppCompatActivity() {

    init{
        Log.i(TAG,"init方法调用了")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var gramarTextView:View =  findViewById(R.id.grammar_tv)
        gramarTextView.setOnClickListener {
            var s = sum(1,2);
            Log.i(TAG,"sum(1,2)=$s")
            Log.i(TAG,"sum(3,4)=${sum(3,4)}")
            Log.i(TAG,"foo()start")
            foo();
            Log.i(TAG,"foo()end")
            C().caller(D())   // 输出 "D.foo in C"
            C1().caller(D())  // 输出 "D.foo in C1" —— 分发接收者虚拟解析
            C().caller(D1())  // 输出 "D.foo in C" —— 扩展接收者静态解析
            C1().caller(D1()) // 输出 "D.foo in C1" —— 分发接收者虚拟解析
        }
    }


}
