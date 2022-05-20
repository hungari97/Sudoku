package com.example.sudoku

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.sudoku.model.data.Cell
import com.example.sudoku.model.data.SelectedGameFunction
import com.example.sudoku.model.data.Table
import com.example.sudoku.model.data.TableType
import com.example.sudoku.utility.forEachCellIndexed
import com.example.sudoku.utility.get
import com.example.sudoku.viewmodels.main.MainViewModel
import com.example.sudoku.viewmodels.main.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sudoku_cell.view.*
import kotlinx.android.synthetic.main.sudoku_cell_section.view.*
import javax.inject.Inject


class TableActivity : AppCompatActivity() {
    private lateinit var viewCellTable: Array<Array<View>>
    private var sudokuType: TableType? = null
    private var isGameFinished = false

    private lateinit var viewModel: MainViewModel

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        viewCellTable = collectTableCells()

        component.inject(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        sudokuType = determinePreselectedTableType()

        viewModel.table.observe(this) { table ->
            onTableLoaded(table)
            viewModel.table.removeObserver(::onTableLoaded)
        }
        viewModel.table.observe(this) { table ->
            onTableRefreshed(table)
        }

        if (sudokuType != null)
            viewModel.loadNewTable(sudokuType!!)
        else
            viewModel.loadPreviousGameState()

        startGameTimer()
    }

    override fun onDestroy() {
        viewModel.cancelTimer()

        if (!isGameFinished) {
            viewModel.saveGameState()
        }
        super.onDestroy()
    }

    private fun collectTableCells(): Array<Array<View>> {
        return arrayOf(
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
    }

    private fun determinePreselectedTableType(): TableType? {
        return if (!intent.getStringExtra("tableType").isNullOrEmpty())
            TableType.valueOf(intent.getStringExtra("tableType")!!)
        else
            null
    }

    private fun onTableRefreshed(table: Table) {
        viewCellTable.forEachCellIndexed { rowIndex, columnIndex, cellView ->
            val cellData = table.cells[rowIndex, columnIndex]
            displayCellData(cellView, cellData)
        }

        if (viewModel.isTableFinished()) {
            endGame()
        }

    }

    private fun onTableLoaded(table: Table) {
        sudokuType = when {
            table.reference.startsWith("N_") -> TableType.NORMAL
            else -> TableType.DIAGONAL
        }
        initializeTableCellListeners()
        initializeNumberSelectorButtons()
        initializeFunctionButtons()
    }

    private fun endGame() {
        isGameFinished = true
        // setup the alert builder
        viewModel.clearGameState()

        val dialog = AlertDialog.Builder(this)
            .setTitle("Congratulations!")
            .setMessage("You did it!")
            .setPositiveButton("Menu") { _, _ ->
                finish()
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
            cellData.chosenNumber == 0 -> {
                setChosenNumber(cellView, cellData.chosenNumber)
                cellData.shownPossibilities.forEachIndexed { tipNumberIndex, isTipNumberShown ->
                    changeCellTip(cellView, tipNumberIndex, isTipNumberShown)
                }
            }
            cellData.isChosenNumberCorrect() -> {
                cellView.cell_number.setTextColor(
                    if (cellData.given) Color.BLUE else Color.BLACK
                )
                setChosenNumber(cellView, cellData.chosenNumber)
            }
            else -> {
                cellView.cell_number.setTextColor(Color.RED)
                setChosenNumber(cellView, cellData.chosenNumber)
            }
        }
    }

    private fun setChosenNumber(cell: View, number: Int) {
        if (number != 0) cell.cell_number.apply {
            text = number.toString()
            visibility = View.VISIBLE
        }
        else cell.cell_number.apply {
            text = "  "
            visibility = View.INVISIBLE
        }

        cell.tips.forEach {
            it.visibility = View.INVISIBLE
        }
    }

    private fun changeCellTip(cell: View, number: Int, visible: Boolean) {
        if (cell.cell_number.visibility == View.VISIBLE)
            return

        val cellTip = cell.tips[number]

        cellTip.visibility =
            if (visible) View.VISIBLE
            else View.INVISIBLE
    }

    private fun initializeNumberSelectorButtons() {
        collectNumberSelectorButtons()
            .forEachIndexed { numberIndex, numberSelectorButton ->
                numberSelectorButton.setOnClickListener { button ->
                    viewModel.selectedNumberIndex = numberIndex
                    button.isSelected = true
                }
            }
    }

    private fun collectNumberSelectorButtons(): List<Button> {
        return listOf(
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
    }


    private fun initializeFunctionButtons() {
        btallPossibilities.setOnClickListener {
            viewModel.showAllPossibilities()
        }

        btDelete.setOnClickListener { button ->
            viewModel.switchFunction(SelectedGameFunction.DELETION)
            updateFunctionButtonBackGrounds()
        }

        btonePossibilities.setOnClickListener {
            viewModel.switchFunction(SelectedGameFunction.GIVE_ANSWER)
            updateFunctionButtonBackGrounds()
        }

        btPencil.setBackgroundResource(R.drawable.edit_off)
        btPencil.setOnClickListener { button ->
            viewModel.switchFunction(SelectedGameFunction.PENCIL)
            updateFunctionButtonBackGrounds()
        }
    }

    private fun updateFunctionButtonBackGrounds(){
        btDelete.setBackgroundColor(
            if (viewModel.selectedFunction == SelectedGameFunction.DELETION)
                Color.YELLOW
            else
                Color.WHITE
        )

        btPencil.setBackgroundResource(
            if (viewModel.selectedFunction == SelectedGameFunction.PENCIL)
                R.drawable.edit_orange
            else
                R.drawable.edit_off
        )

        btonePossibilities.setBackgroundColor(
            if (viewModel.selectedFunction == SelectedGameFunction.GIVE_ANSWER)
                Color.YELLOW
            else
                Color.WHITE
        )

    }

    private fun setBackgroundNormal() {
        viewCellTable.forEachCellIndexed { rowIndex, columnIndex, cellView ->
            cellView.setBackgroundResource(R.drawable.cell_border)
            if (sudokuType == TableType.DIAGONAL && isPositionOnDiagonal(rowIndex, columnIndex))
                cellView.setBackgroundResource(R.drawable.diagonal_cell_background)
        }
    }

    private fun initializeTableCellListeners() {
        viewCellTable.forEachCellIndexed { rowIndex, columnIndex, cellView ->
            cellView.setOnClickListener { cell ->
                viewModel.selectPosition(rowIndex, columnIndex)
                updateCellBackGround(rowIndex, columnIndex, cell)
            }
            if (sudokuType == TableType.DIAGONAL && isPositionOnDiagonal(rowIndex, columnIndex))
                cellView.setBackgroundResource(R.drawable.diagonal_cell_background)
        }
    }

    private fun updateCellBackGround(selectedRow: Int, selectedColumn: Int, selectedCell: View) {
        setBackgroundNormal()
        for (indexInLine in 0 until 9) {
            viewCellTable[selectedRow, indexInLine].setBackgroundResource(R.drawable.selected_row_or_column_cell)
            viewCellTable[indexInLine, selectedColumn].setBackgroundResource(R.drawable.selected_row_or_column_cell)
        }
        selectedCell.setBackgroundResource(R.drawable.cell_border)

        if (sudokuType == TableType.DIAGONAL && isPositionOnDiagonal(selectedRow, selectedColumn))
            selectedCell.setBackgroundResource(R.drawable.diagonal_cell_background)
    }

    private fun isPositionOnDiagonal(rowIndex: Int, columnIndex: Int) =
        rowIndex == columnIndex || rowIndex + columnIndex == 8

    @SuppressLint("SetTextI18n")
    private fun startGameTimer() {
        viewModel.startTimeCounter()
        viewModel.count.observe(this) { secondsPassed ->
            val minute = secondsPassed / 60
            val second = secondsPassed % 60
            runOnUiThread {
                tvTime.text = "$minute:$second"
            }
        }
    }

}