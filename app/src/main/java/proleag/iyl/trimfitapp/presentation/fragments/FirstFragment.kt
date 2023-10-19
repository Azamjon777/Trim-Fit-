package proleag.iyl.trimfitapp.presentation.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.PopupMenu
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import proleag.iyl.trimfitapp.R
import proleag.iyl.trimfitapp.databinding.DialogGoalWeightBinding
import proleag.iyl.trimfitapp.databinding.FragmentFirstBinding
import proleag.iyl.trimfitapp.presentation.DataActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding
    private lateinit var sharedPreferences: SharedPreferences
//    private val viewModel: FirstViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        val rootView = binding.root
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checked()
        binding.genderIcon.setOnClickListener {
            showPopupMenu(it)
        }
        binding.birthdayIcon.setOnClickListener {
            showDatePicker()
        }
        binding.kgImg.setOnClickListener {
            showWeightPicker()
        }
        binding.heightImg.setOnClickListener {
            showHeightPicker()
        }
        binding.stepsImg.setOnClickListener {
            showStepsPicker()
        }
        binding.goalWeightImg.setOnClickListener {
            showGoalWeightDialog()
        }
        binding.btnNext.setOnClickListener {
            checked()
        }
    }

    private fun checked() {
        val sharedPreferences =
            requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val savedGender = sharedPreferences.getString("selected_gender", "")
        val savedBirthday = sharedPreferences.getLong("selected_birthday", 0)
        val currentWeight = sharedPreferences.getInt("selected_weight", 0)
        val currentHeight = sharedPreferences.getInt("selected_height", 0)
        val currentSteps = sharedPreferences.getInt("selected_steps", 0)
        val currentGoalWeight = sharedPreferences.getInt("goal_weight", 0)

        if (savedGender.isNullOrBlank() || savedBirthday == 0L || currentWeight == 0 ||
            currentHeight == 0 || currentSteps == 0 || currentGoalWeight == 0
        ) {
            Toast.makeText(
                requireContext(),
                getString(R.string.request),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val intent = Intent(requireContext(), DataActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showGoalWeightDialog() {
        val currentGoalWeight = sharedPreferences.getInt("goal_weight", 0)

        val bindingItem = DialogGoalWeightBinding.inflate(layoutInflater)
        val dialogView = bindingItem.root

        bindingItem.seekBarGoalWeight.progressDrawable =
            resources.getDrawable(R.drawable.custom_seekbar_progress)
        bindingItem.seekBarGoalWeight.thumb = resources.getDrawable(R.drawable.custom_seekbar_thumb)

        bindingItem.seekBarGoalWeight.progress = currentGoalWeight - 50
        bindingItem.textViewSelectedWeight.text =
            getString(R.string.weight_format, currentGoalWeight)

        bindingItem.seekBarGoalWeight.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val selectedWeight = progress + 50
                bindingItem.textViewSelectedWeight.text =
                    getString(R.string.weight_format, selectedWeight)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val goalWeightDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.set_weight_goal))
            .setView(dialogView)
            .setPositiveButton(getString(R.string.save)) { dialog, _ ->
                val selectedWeight = bindingItem.seekBarGoalWeight.progress + 50
                saveGoalWeight(selectedWeight)
                binding.tvGoalWeight.text =
                    getString(R.string.weight_format, selectedWeight)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        goalWeightDialog.show()
    }

    private fun showStepsPicker() {
        val stepsArray = resources.getStringArray(R.array.steps)
        val currentStepsIndex = sharedPreferences.getInt("selected_steps", 0)

        val stepsPickerDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.select_steps))
            .setSingleChoiceItems(stepsArray, currentStepsIndex) { dialog, which ->
                val selectedSteps = (which + 1) * 1000
                binding.stepsByDay.text = getString(R.string.steps_format, selectedSteps)
                saveSteps(selectedSteps)
                dialog.dismiss()
            }
            .create()

        stepsPickerDialog.show()
    }

    private fun showWeightPicker() {
        val currentWeight = sharedPreferences.getInt("selected_weight", 0)

        val weightPickerDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.select_weight))
            .setSingleChoiceItems(
                R.array.weights, currentWeight - 40
            ) { dialog, which ->
                val selectedWeight = which + 40
                binding.tvKg.text = getString(R.string.weight_format, selectedWeight)
                saveWeight(selectedWeight)
                dialog.dismiss()
            }
            .create()

        weightPickerDialog.show()
    }

    private fun showHeightPicker() {
        val currentHeight =
            sharedPreferences.getInt("selected_height", 0)

        val heightPickerDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.select_height))
            .setSingleChoiceItems(
                R.array.heights, currentHeight - 100
            ) { dialog, which ->
                val selectedHeight = which + 100
                binding.tvHeight.text = getString(R.string.height_format, selectedHeight)
                saveHeight(selectedHeight)
                dialog.dismiss()
            }
            .create()

        heightPickerDialog.show()
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.gender_menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_male -> {
                    binding.genderText.text = getString(R.string.male)
                    saveGender(getString(R.string.male))
                    true
                }

                R.id.menu_female -> {
                    binding.genderText.text = getString(R.string.female)
                    saveGender(getString(R.string.female))
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                binding.tvBirthday.text = sdf.format(selectedDate.time)

                saveBirthday(selectedDate.timeInMillis)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    private fun saveGender(gender: String) {
        val editor = sharedPreferences.edit()
        editor.putString("selected_gender", gender)
        editor.apply()
    }

    private fun saveBirthday(birthday: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong("selected_birthday", birthday)
        editor.apply()
    }

    private fun saveWeight(weight: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("selected_weight", weight)
        editor.apply()
    }

    private fun saveHeight(height: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("selected_height", height)
        editor.apply()
    }

    private fun saveSteps(steps: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("selected_steps", steps)
        editor.apply()
    }

    private fun saveGoalWeight(weight: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("goal_weight", weight)
        editor.apply()
    }
}