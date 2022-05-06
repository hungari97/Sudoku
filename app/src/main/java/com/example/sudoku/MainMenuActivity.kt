package com.example.sudoku

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.sudoku.database.DBConnector
import com.example.sudoku.model.data.TableType
import kotlinx.android.synthetic.main.activity_main_menu.*

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        btPlay.setOnClickListener { onPlayButtonClicked() }

        btContinue.setOnClickListener { onContinueButtonClicked() }

        btCreate.setOnClickListener { onCreateButtonClicked() }
    }

    private fun onPlayButtonClicked() {
        val intent = Intent(
            this,
            TableActivity::class.java
        )
        // setup the alert builder
        val dialog: AlertDialog =
            AlertDialog.Builder(this)
                .setTitle("Select Mode!")
                .setMessage("What mode would you like to play?")
                .setPositiveButton("Diagonal") { _, _ ->
                    intent.putExtra("tableType", TableType.DIAGONAL.name)
                    startActivity(intent)
                }
                .setNegativeButton("Normal") { _, _ ->
                    intent.putExtra("tableType", TableType.NORMAL.name)
                    startActivity(intent)
                }
                .create()

        dialog.show()
    }

    private fun onContinueButtonClicked() {
        val intent = Intent(
            this,
            TableActivity::class.java
        )
        startActivity(intent)
    }

    private fun onCreateButtonClicked() {
        val intent = Intent(
            this,
            CreateTableActivity::class.java
        )

        val dialog: AlertDialog = AlertDialog.Builder(this)
            .setTitle("Select Mode!")
            .setMessage("What table want to create?")
            .setPositiveButton("Diagonal") { _, _ ->
                intent.putExtra("tableType", TableType.DIAGONAL.name)
                startActivity(intent)
            }
            .setNegativeButton("Normal") { _, _ ->
                intent.putExtra("tableType", TableType.NORMAL.name)
                startActivity(intent)
            }.create()

        dialog.show()
    }
}