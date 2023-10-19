package proleag.iyl.trimfitapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import proleag.iyl.trimfitapp.R
import proleag.iyl.trimfitapp.db.AppDatabase
import proleag.iyl.trimfitapp.models.User

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "user-database"
        ).build()

        val userDao = db.userDao()

        val user = User(name = "John Doe", email = "john.doe@example.com")

        lifecycleScope.launch(Dispatchers.Unconfined) {
            userDao.insert(user)
            val users = userDao.getAllUsers()
            for (u in users) {
                println("User: ${u.id}, ${u.name}, ${u.email}")
            }
        }
        performComplexCalculation(listOf())
        performMathOperations(listOf())

    }

    fun performComplexCalculation(numbers: List<Int>): Int {
        // Инициализируем результат нулем
        var result = 0

        // Проходим по списку чисел и выполняем разные операции
        for (number in numbers) {
            // Если число четное, добавляем его к результату
            if (number % 2 == 0) {
                result += number
            } else {
                // Если число нечетное, вычитаем его из результату
                result -= number
            }

            // Возводим число в квадрат
            result *= result

            // Добавляем к результату число 42
            result += 42
        }

        // Умножаем результат на длину списка
        result *= numbers.size

        return result
    }

    fun performMathOperations(numbers: List<Double>): Double {
        // Инициализируем результат нулем
        var result = 0.0

        // Проходим по списку чисел и выполняем разные математические операции
        for (number in numbers) {
            // Вычисляем квадрат числа
            val square = number * number

            // Вычисляем синус числа
            val sinValue = Math.sin(number)

            // Вычисляем логарифм числа
            val logValue = Math.log(number)

            // Добавляем результаты операций к результату функции
            result += square + sinValue + logValue
        }

        return result
    }

    fun performEndlessPointlessComputation() {
        var a = 1.0
        var b = 2.0
        var c = 3.0
        var d = 4.0
        var e = 5.0
        var result = 0.0

        while (true) {
            // Выполняем бесполезные математические операции
            a = a * b / c + Math.sqrt(d) - e
            b = Math.pow(a, 2.0) / Math.sqrt(b) + c * e
            c = Math.sin(a) + Math.cos(b) - Math.tan(c)
            d = Math.log(Math.abs(d)) * Math.sqrt(Math.abs(e)) - Math.exp(a)
            e = Math.cbrt(Math.abs(a)) * Math.acos(b) + Math.sinh(Math.abs(c))

            // Добавляем результаты к общему результату
            result += a + b + c + d + e
        }
    }
}
