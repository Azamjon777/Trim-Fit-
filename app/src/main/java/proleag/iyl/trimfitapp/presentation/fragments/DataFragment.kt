package proleag.iyl.trimfitapp.presentation.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import proleag.iyl.trimfitapp.R
import proleag.iyl.trimfitapp.databinding.FragmentDataBinding
import proleag.iyl.trimfitapp.viewmodel.StepCountViewModel

class DataFragment : Fragment(), SensorEventListener {

    private var currentStepsTotal: Float? = null
    private var _binding: FragmentDataBinding? = null
    private val binding get() = _binding!!
    private var sensorManager: SensorManager? = null
    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var stepCountViewModel: StepCountViewModel

    private val PERMISSION_REQUEST_ACTIVITY_RECOGNITION = 100

    private val caloriesPerStep = 0.04f
    private val stepLengthMeters = 0.762f

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataBinding.inflate(inflater, container, false)
        if (!hasActivityRecognitionPermission()) {
            requestActivityRecognitionPermission()
        }
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        initial()

        viewModelSetup()

        binding.btnReset.setOnClickListener {
            previousTotalSteps = totalSteps
            stepCountViewModel.setStepCount(1)
        }
        binding.btnResetGoalSteps.setOnClickListener {
            val stepsArray = resources.getStringArray(R.array.steps)
            val currentSelectedSteps = sharedPreferences.getInt("selected_steps", 1000)

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Select Goal Steps")
            builder.setSingleChoiceItems(stepsArray, currentSelectedSteps / 1000) { dialog, which ->
                val selectedSteps = (which + 1) * 1000
                val editor = sharedPreferences.edit()
                editor.putInt("selected_steps", selectedSteps)
                editor.apply()
                binding.tvTotalMax.text = "/$selectedSteps"

                binding.progressCircular.progressMax = selectedSteps.toFloat()

                dialog.dismiss()
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            builder.create().show()
        }
    }

    private fun initial() {
        sharedPreferences =
            requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val currentSteps = sharedPreferences.getInt("selected_steps", 0)
        binding.progressCircular.progressMax = currentSteps.toFloat()
        binding.tvTotalMax.text = "/$currentSteps"
    }

    @SuppressLint("SetTextI18n")
    private fun viewModelSetup() {
        stepCountViewModel = ViewModelProvider(this)[StepCountViewModel::class.java]

        stepCountViewModel.stepCount.observe(viewLifecycleOwner) { count ->
            binding.tvStepsTaken.text = count.toString()
            val burnedCalories = count * caloriesPerStep
            binding.tvBurnedCalories.text = String.format("%.2f", burnedCalories)
            val distanceKilometers = (count * stepLengthMeters) / 1000
            binding.tvDistance.text = String.format("%.2f", distanceKilometers) + " km"
            sharedPreferences.edit().putFloat("km", distanceKilometers)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (running) {
            totalSteps = event!!.values[0]
            currentStepsTotal = totalSteps - previousTotalSteps
            stepCountViewModel.setStepCount(currentStepsTotal!!.toInt())

            val editor = sharedPreferences.edit()
            editor.putInt("current_steps_for_profile", currentStepsTotal!!.toInt())
            editor.apply()

            stepCountViewModel.stepCount.observe(viewLifecycleOwner) { count ->
                binding.progressCircular.apply {
                    setProgressWithAnimation(count.toFloat())
                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    override fun onResume() {
        super.onResume()
        running = true
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {
            Toast.makeText(
                requireActivity(),
                getString(R.string.no_sensor_detected_on_this_device),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestActivityRecognitionPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
            PERMISSION_REQUEST_ACTIVITY_RECOGNITION
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun hasActivityRecognitionPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACTIVITY_RECOGNITION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}