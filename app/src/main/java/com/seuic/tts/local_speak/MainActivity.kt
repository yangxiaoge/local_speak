package com.seuic.tts.local_speak

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.seuic.tts.local_speak.voice.VoicePlay

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val editText = findViewById<AppCompatEditText>(R.id.text_et)
        findViewById<AppCompatButton>(R.id.speak_btn).setOnClickListener {

            editText.text?.trim()?.let {
                if (it.isNotEmpty()) {
                    val voicePlay = VoicePlay.with(this)
                    it.forEach { item ->
                        when (item.toUpperCase()) {
                            'A' -> voicePlay.playVoice(R.raw.a)
                            'B' -> voicePlay.playVoice(R.raw.b)
                            'C' -> voicePlay.playVoice(R.raw.c)
                            'D' -> voicePlay.playVoice(R.raw.d)
                            'E' -> voicePlay.playVoice(R.raw.e)
                            'F' -> voicePlay.playVoice(R.raw.f)


                            '1' -> voicePlay.playVoice(R.raw.n1)
                            '2' -> voicePlay.playVoice(R.raw.n2)
                            '3' -> voicePlay.playVoice(R.raw.n3)
                            '4' -> voicePlay.playVoice(R.raw.n4)
                            '5' -> voicePlay.playVoice(R.raw.n5)
                            '6' -> voicePlay.playVoice(R.raw.n6)
                        }
                    }
                }
            }
        }
    }
}