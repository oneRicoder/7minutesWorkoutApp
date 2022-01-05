package com.example.a7minutesworkout

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkout.databinding.ActivityExerciseBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding:  ActivityExerciseBinding? = null
    private var restTimer: CountDownTimer? = null
    private var restProgress = 0

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0

    private var exerciseList:ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null

    private var exerciseAdapter: ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = Constants.defaultExerciseList()

        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }
        setUpRestView()

        tts = TextToSpeech(this,this)
        setupExerciseStatusRecyclerView()

    }

    private fun setupExerciseStatusRecyclerView(){
        binding?.rvExerciseStatus?.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun setUpRestView(){

        try {
            val soundUri = Uri.parse("android.resource://com.example.a7minutesworkout/"+R.raw.press_start)
            player = MediaPlayer.create(applicationContext,soundUri)
            player?.isLooping = false
            player?.start()
        }catch (e: Exception){
            e.printStackTrace()
        }

        binding?.flRestView?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE
        binding?.tvUpComingLabel?.visibility = View.VISIBLE
        binding?.tvUpComingExerciseName?.visibility = View.VISIBLE
        if(restTimer!=null){
            restTimer?.cancel()
            restProgress = 0
        }

        binding?.tvUpComingExerciseName?.text = exerciseList!![currentExercisePosition+1].getName()
        speakOut("Upcoming Exercise is"+exerciseList!![currentExercisePosition+1].getName())
        restProgressBar()

    }
    private fun setUpExerciseView(){
        binding?.flRestView?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE
        binding?.tvUpComingLabel?.visibility = View.INVISIBLE
        binding?.tvUpComingExerciseName?.visibility = View.INVISIBLE
        if (exerciseTimer!=null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()
        setExerciseProgressBar()

        speakOut("Now you have to perform"+exerciseList!![currentExercisePosition].getName())

    }
    private fun restProgressBar(){
        binding?.progressBar?.progress = restProgress

        restTimer = object : CountDownTimer(10000,1000){
            override fun onTick(p0: Long) {
                restProgress++
                binding?.progressBar?.progress = 10 - restProgress
                binding?.tvTimer?.text = (10-restProgress).toString()
            }
            override fun onFinish() {
//                Toast.makeText(this@ExerciseActivity,"Now We Will Start The Exercise",Toast.LENGTH_SHORT).show()
                currentExercisePosition++
                setUpExerciseView()
            }
        }.start()
    }

    private fun setExerciseProgressBar(){
        binding?.progressBarExercise?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(30000,1000){
            override fun onTick(p0: Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress = 30 - exerciseProgress
                binding?.tvTimerExercise?.text = (30-exerciseProgress).toString()
            }
            override fun onFinish() {
                //Toast.makeText(this@ExerciseActivity,"30 Seconds Are Over, Lets Go To The Rest View",Toast.LENGTH_SHORT).show()
                if (currentExercisePosition<exerciseList?.size!!-1){
                    setUpRestView()
                }else{
                    Toast.makeText(this@ExerciseActivity,"Congratulations! You Have Completed 7 Minutes Workout",Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(restTimer!=null){
            restTimer?.cancel()
            restProgress = 0
        }
        if (exerciseTimer!=null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        if (tts!=null){
            tts?.stop()
            tts?.shutdown()
        }
        if(player!=null){
            player!!.stop()
        }
        binding = null
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            var result = tts!!.setLanguage(Locale("hi","IN"))
            if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA){
                Log.e("TTS","language not supported")
            }else{
                Log.e("TTS","Initialization failed")
            }
        }
    }

    private fun speakOut(text: String){
        tts?.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }
}