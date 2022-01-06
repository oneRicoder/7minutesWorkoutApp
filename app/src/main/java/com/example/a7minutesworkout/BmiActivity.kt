package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.a7minutesworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BmiActivity : AppCompatActivity() {

    private var binding: ActivityBmiBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarBmiActivity)

        if (supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarBmiActivity?.setNavigationOnClickListener {
            onBackPressed()
        }
        binding?.btnCalculateUnits?.setOnClickListener {
            if (validateMetricUnits()){
                val heightValue: Float = binding?.etMetricUnitHeight?.text.toString().toFloat() / 100
                val weightValue: Float = binding?.etMetricUnitWeight?.text.toString().toFloat()
                val bmi = weightValue / (heightValue*heightValue)
                displayBMIResult(bmi)
            }else{
                Toast.makeText(this@BmiActivity,"Please Fill the Boxes",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayBMIResult(bmi:Float){
        binding?.llDisplayBMIResult?.visibility = View.VISIBLE
        binding?.tvBMIValue?.text = bmi.toString()
        if (bmi<18.5){
            val bmiValue = BigDecimal(bmi.toDouble()).setScale(2,RoundingMode.HALF_EVEN).toString()
            binding?.tvBMIValue?.text = bmiValue
            binding?.tvBMIType?.text = "You Are Under Weighted"
            binding?.tvBMIDescription?.text = "You are Patlu, Eat More"
        }else if (bmi >= 18.5 && bmi < 25){
            val bmiValue = BigDecimal(bmi.toDouble()).setScale(2,RoundingMode.HALF_EVEN).toString()
            binding?.tvBMIValue?.text = bmiValue
            binding?.tvBMIType?.text = "You Have Normal Weight"
            binding?.tvBMIDescription?.text = "You are Fit and Fine"
        }else if (bmi>=25){
            val bmiValue = BigDecimal(bmi.toDouble()).setScale(2,RoundingMode.HALF_EVEN).toString()
            binding?.tvBMIValue?.text = bmiValue
            binding?.tvBMIType?.text = "You Are Over Weighted"
            binding?.tvBMIDescription?.text = "You are Motu, Eat Less"
        }
    }
    private fun validateMetricUnits():Boolean{
        var isValid = true
        if (binding?.etMetricUnitWeight?.text.toString().isEmpty()){
            isValid = false
        }else if (binding?.etMetricUnitHeight?.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }
}