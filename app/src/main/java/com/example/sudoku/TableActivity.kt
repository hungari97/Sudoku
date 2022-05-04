package com.example.sudoku

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.sudoku.model.data.Cell
import com.example.sudoku.model.data.TableType
import com.example.sudoku.utility.forEachCellIndexed
import com.example.sudoku.utility.get
import com.example.sudoku.utility.mapInPlace
import com.example.sudoku.viewmodels.main.MainViewModel
import com.example.sudoku.viewmodels.main.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sudoku_cell.view.*
import kotlinx.android.synthetic.main.sudoku_cell_section.view.*
import java.util.*
import javax.inject.Inject


class TableActivity : AppCompatActivity() {
    private var onFocusButton = BooleanArray(9) { false }
    private lateinit var viewCellTable: Array<Array<View>>
    private lateinit var tableType: TableType

    private lateinit var viewModel: MainViewModel

    private lateinit var timer: Timer

    @Inject
    lateinit var factory: MainViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        component.inject(this)

        tableType = if (intent.getStringExtra("tableType").isNullOrEmpty()) {
            TableType.NORMAL
        } else {
            TableType.valueOf(intent.getStringExtra("tableType")!!)
        }

        viewModel = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)

        viewCellTable = arrayOf(
            arrayOf(
                section1.full_cell_1,
                section1.full_cell_2,
                section1.full_cell_3,
                section2.full_cell_1,
                section2.full_cell_2,
                section2.full_cell_3,
                section3.full_cell_1,
                section3.full_cell_2,
                section3.full_cell_3
            ),
            arrayOf(
                section1.full_cell_4,
                section1.full_cell_5,
                section1.full_cell_6,
                section2.full_cell_4,
                section2.full_cell_5,
                section2.full_cell_6,
                section3.full_cell_4,
                section3.full_cell_5,
                section3.full_cell_6
            ),
            arrayOf(
                section1.full_cell_7,
                section1.full_cell_8,
                section1.full_cell_9,
                section2.full_cell_7,
                section2.full_cell_8,
                section2.full_cell_9,
                section3.full_cell_7,
                section3.full_cell_8,
                section3.full_cell_9
            ),
            arrayOf(
                section4.full_cell_1,
                section4.full_cell_2,
                section4.full_cell_3,
                section5.full_cell_1,
                section5.full_cell_2,
                section5.full_cell_3,
                section6.full_cell_1,
                section6.full_cell_2,
                section6.full_cell_3
            ),
            arrayOf(
                section4.full_cell_4,
                section4.full_cell_5,
                section4.full_cell_6,
                section5.full_cell_4,
                section5.full_cell_5,
                section5.full_cell_6,
                section6.full_cell_4,
                section6.full_cell_5,
                section6.full_cell_6
            ),
            arrayOf(
                section4.full_cell_7,
                section4.full_cell_8,
                section4.full_cell_9,
                section5.full_cell_7,
                section5.full_cell_8,
                section5.full_cell_9,
                section6.full_cell_7,
                section6.full_cell_8,
                section6.full_cell_9
            ),
            arrayOf(
                section7.full_cell_1,
                section7.full_cell_2,
                section7.full_cell_3,
                section8.full_cell_1,
                section8.full_cell_2,
                section8.full_cell_3,
                section9.full_cell_1,
                section9.full_cell_2,
                section9.full_cell_3
            ),
            arrayOf(
                section7.full_cell_4,
                section7.full_cell_5,
                section7.full_cell_6,
                section8.full_cell_4,
                section8.full_cell_5,
                section8.full_cell_6,
                section9.full_cell_4,
                section9.full_cell_5,
                section9.full_cell_6
            ),
            arrayOf(
                section7.full_cell_7,
                section7.full_cell_8,
                section7.full_cell_9,
                section8.full_cell_7,
                section8.full_cell_8,
                section8.full_cell_9,
                section9.full_cell_7,
                section9.full_cell_8,
                section9.full_cell_9
            )
        )

        initializeTableCellsListeners()
        buttonsInitialize()

        viewModel.getInitialiseTable(tableType)


        viewModel.currentTable.observe(this) { table ->
            viewCellTable.forEachCellIndexed { rowIndex, columnIndex, cellView ->
                val cellData = table.cells[rowIndex, columnIndex]
                displayCellData(cellView, cellData)
            }
            if (viewModel.isTableFinished()) {
                endTable()
            }
        }
        startTimeCounter()
    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }

    private fun endTable() {
        val intent = Intent(
            this,
            MainMenuActivity::class.java
        )
        // setup the alert builder
        val dialog = AlertDialog.Builder(this)
            .setTitle("Congratulations!")
            .setMessage("You did it!")
            .setPositiveButton("Menu") { _, _ ->
                startActivity(intent)
            }
            .create()

        dialog.show()
    }

    private val View.tips
        get() = listOf(
            cell_tip_1,
            cell_tip_2,
            cell_tip_3,
            cell_tip_4,
            cell_tip_5,
            cell_tip_6,
            cell_tip_7,
            cell_tip_8,
            cell_tip_9
        )

    private fun displayCellData(cellView: View, cellData: Cell) {
        when {
            cellData.checkChosenNumber() -> {
                if (cellData.given) {
                    cellView.cell_number.setTextColor(Color.BLUE)
                } else {
                    cellView.cell_number.setTextColor(Color.BLACK)
                }
                writeNumber(
                    cellView,
                    cellData.chosenNumber
                )
            }
            cellData.chosenNumber == 0 -> {
                writeNumber(
                    cellView,
                    cellData.chosenNumber
                )
                cellData.shownPossibilities.forEachIndexed { numberIndex, isNumberShown ->
                    pencilNumberChange(cellView, numberIndex, isNumberShown)
                }
            }
            else -> {
                cellView.cell_number.setTextColor(Color.RED)
                writeNumber(
                    cellView,
                    cellData.chosenNumber
                )
            }
        }
    }

    private fun writeNumber(cell: View, number: Int) {
        cell.cell_number.text = if (number != 0) {
            number.toString()
        } else {
            " "
        }

        cell.cell_number.visibility = if (number != 0) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }

        cell.tips.forEach {
            it.visibility = View.INVISIBLE
        }
    }

    private fun pencilNumberChange(cell: View, number: Int, visible: Boolean) {
        if (cell.cell_number.visibility == View.INVISIBLE) {
            val tipCell = cell.tips[number]

            tipCell.visibility = if (visible) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }
    }

    private fun buttonsInitialize() {
        val btAnswerList = listOf(
            btAnswer1,
            btAnswer2,
            btAnswer3,
            btAnswer4,
            btAnswer5,
            btAnswer6,
            btAnswer7,
            btAnswer8,
            btAnswer9
        )

        btAnswerList.forEachIndexed { index, button ->
            button.setOnClickListener {
                onFocusButton.mapInPlace { false }
                onFocusButton[index] = true
                it.setBackgroundResource(R.drawable.number_selector_background)
            }
        }

        btallPossibilities.setOnClickListener {
            viewModel.showAllPossibilities()
        }

        btDelete.setOnClickListener {
            viewModel.changeDelete()
            if (viewModel.isDelete())
                it.setBackgroundColor(Color.YELLOW)
            else
                it.setBackgroundColor(Color.WHITE)
        }

        btonePossibilities.setOnClickListener {
            viewModel.askPossibleTip()
        }
        btPencil.setBackgroundResource(R.drawable.edit_off)
        btPencil.setOnClickListener {
            viewModel.changePencil()
            if (viewModel.isPencil())
                it.setBackgroundResource(R.drawable.edit_orange)
            else
                it.setBackgroundResource(R.drawable.edit_off)
        }
    }

    private fun setBackgroundNormal() {
        viewCellTable.forEachCellIndexed { rowIndex, columnIndex, cellView ->
            cellView.setBackgroundResource(R.drawable.cell_border)
            when (tableType) {
                TableType.DIAGONAL -> {
                    if (rowIndex == columnIndex || rowIndex + columnIndex == 8)
                        cellView.setBackgroundResource(R.drawable.diagonal_cell_background)
                }
                TableType.NORMAL -> {}
            }
        }
    }

    private fun initializeTableCellsListeners() {

        viewCellTable.forEachCellIndexed { rowIndex, columnIndex, cellView ->
            cellView.setOnClickListener {
                if (!viewModel.givenNumber(rowIndex * 9 + columnIndex)) {
                    when {
                        viewModel.isGiveAnswer() -> {
                            viewModel.givePossibleTip(Pair(rowIndex, columnIndex))
                        }
                        viewModel.isDelete() -> {
                            onFocusButton.fill(false)
                            viewModel.hideNumber(rowIndex, columnIndex)
                        }
                        onFocusButton.contains(true) -> {
                            viewModel.writeAnswer(
                                Pair(rowIndex, columnIndex),
                                onFocusButton.indexOf(true) + 1
                            )
                        }
                    }
                }
                setBackgroundNormal()
                for (i in 0 until 9) {
                    viewCellTable[rowIndex, i].setBackgroundResource(R.drawable.selected_row_or_column_cell)
                    viewCellTable[i, columnIndex].setBackgroundResource(R.drawable.selected_row_or_column_cell)

                }
                cellView.setBackgroundResource(R.drawable.cell_border)

                when (tableType) {
                    TableType.DIAGONAL -> {
                        if (rowIndex == columnIndex || rowIndex + columnIndex == 8)
                            cellView.setBackgroundResource(R.drawable.diagonal_cell_background)
                    }
                    TableType.NORMAL -> {}
                }
            }
            when (tableType) {
                TableType.DIAGONAL -> {
                    if (rowIndex == columnIndex || rowIndex + columnIndex == 8)
                        cellView.setBackgroundResource(R.drawable.diagonal_cell_background)
                }
                TableType.NORMAL -> {}
            }


        }
    }

    private fun startTimeCounter() {
        timer = Timer()
        var count = 0
        timer.scheduleAtFixedRate(object : TimerTask() {
            @SuppressLint("SetTextI18n")
            override fun run() {
                val cp = (count).floorDiv(60)
                val cm = count % 60
                runOnUiThread {
                    tvTime.text = "$cp:$cm"
                }
                count++
            }
        }, 1000, 1000)
    }


}