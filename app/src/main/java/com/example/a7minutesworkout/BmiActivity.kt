package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.a7minutesworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BmiActivity : AppCompatActivity() {

    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
        private const val US_UNITS_VIEW = "US_UNIT_VIEW"
    }
    private var currentVisibleView: String = METRIC_UNITS_VIEW
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
        makeVisibleMetricsUnitsView()
        binding?.rgUnits?.setOnCheckedChangeListener { _, checkedId: Int ->

            if (checkedId == R.id.rbMetricUnits){
                makeVisibleMetricsUnitsView()
            }else if (checkedId == R.id.rbUsUnits){
                makeVisibleUsUnitsView()
            }
        }

        binding?.btnCalculateUnits?.setOnClickListener {
            if (currentVisibleView == METRIC_UNITS_VIEW){
                if (validateMetricUnits()){
                    val heightValue: Float = binding?.etMetricUnitHeight?.text.toString().toFloat() / 100
                    val weightValue: Float = binding?.etMetricUnitWeight?.text.toString().toFloat()
                    val bmi = weightValue / (heightValue*heightValue)
                    displayBMIResult(bmi)
                }else{
                    Toast.makeText(this@BmiActivity,"Please Fill the Boxes",Toast.LENGTH_SHORT).show()
                }
            }else{
                if (validateUsUnits()){
                    val weightValue: Float = binding?.etMetricUnitWeight?.text.toString().toFloat()
                    val feetValue: Float = binding?.etUsMetricHeightFeet?.text.toString().toFloat()
                    val inchValue: Float = binding?.etUsMetricHeightInch?.text.toString().toFloat()
                    val heightValue = ((feetValue*30.48)+(inchValue*2.54))/100
                    val bmi = (weightValue / (heightValue*heightValue)).toFloat()
                    displayBMIResult(bmi)
                }else{
                    Toast.makeText(this@BmiActivity,"Please Fill the Boxes",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun makeVisibleMetricsUnitsView(){
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.tilMetricUnitHeight?.visibility = View.VISIBLE
        binding?.tilMetricUnitWeight?.visibility = View.VISIBLE
        binding?.tilMetricUsUnitHeightFeet?.visibility = View.GONE
        binding?.tilMetricUsUnitHeightInch?.visibility = View.GONE

        binding?.etMetricUnitHeight?.text!!.clear()
        binding?.etMetricUnitWeight?.text!!.clear()

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE
    }
    private fun makeVisibleUsUnitsView(){
        currentVisibleView = US_UNITS_VIEW
        binding?.tilMetricUnitHeight?.visibility = View.INVISIBLE
        binding?.tilMetricUnitWeight?.visibility = View.VISIBLE
        binding?.tilMetricUsUnitHeightFeet?.visibility = View.VISIBLE
        binding?.tilMetricUsUnitHeightInch?.visibility = View.VISIBLE

        binding?.etMetricUnitWeight?.text!!.clear()
        binding?.etUsMetricHeightFeet?.text!!.clear()
        binding?.etUsMetricHeightInch?.text!!.clear()

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE
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
    private fun validateUsUnits():Boolean{
        var isValid = true
        if (binding?.etMetricUnitWeight?.text.toString().isEmpty()){
            isValid = false
        }else if (binding?.etUsMetricHeightFeet?.text.toString().isEmpty()){
            isValid = false
        }else if (binding?.etUsMetricHeightInch?.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }
}