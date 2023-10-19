package proleag.iyl.trimfitapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StepCountViewModel : ViewModel() {
    private val _stepCount = MutableLiveData<Int>()
    val stepCount: LiveData<Int>
        get() = _stepCount

    fun setStepCount(count: Int) {
        _stepCount.value = count
    }
}
