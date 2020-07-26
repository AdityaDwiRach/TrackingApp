package com.adr.trackingapp

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.adr.trackingapp.adapter.HistoryRunningAdapter
import kotlinx.android.synthetic.main.activity_history_running.*

class HistoryRunningActivity : AppCompatActivity() {

    private var isFabExpanded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_running)

        val adapter = HistoryRunningAdapter()
        rv_history_running.setHasFixedSize(true)
        rv_history_running.layoutManager = LinearLayoutManager(this)
        rv_history_running.adapter = adapter

        fab_historyactivity.setOnClickListener {
            if (isFabExpanded){
                collapsedFabMenu()
            } else {
                expandFabMenu()
            }
        }

        fab_history_to_main.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun expandFabMenu() {
        ViewCompat.animate(fab_historyactivity).rotation(180.0f).withLayer().setDuration(300)
            .start()
        val openAnim = AnimationUtils.loadAnimation(this, R.anim.open_fab_anim)
        ll_run.startAnimation(openAnim)
        isFabExpanded = true
    }

    private fun collapsedFabMenu() {
        ViewCompat.animate(fab_historyactivity).rotation(0.0f).withLayer().setDuration(300)
            .start()
        val closeAnim = AnimationUtils.loadAnimation(this, R.anim.close_fab_anim)
        ll_run.startAnimation(closeAnim)
        isFabExpanded = false
    }
}
