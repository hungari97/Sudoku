package com.example.sudoku

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.example.sudoku.model.data.TableType
import com.example.sudoku.utility.forEachCellIndexed
import com.example.sudoku.utility.get
import com.example.sudoku.utility.mapInPlace
import com.example.sudoku.viewmodels.create.CreateViewModel
import com.example.sudoku.viewmodels.create.CreateViewModelFactory
import kotlinx.android.synthetic.main.activity_create_table.*
import kotlinx.android.synthetic.main.activity_main.btAnswer1
import kotlinx.android.synthetic.main.activity_main.btAnswer2
import kotlinx.android.synthetic.main.activity_main.btAnswer3
import kotlinx.android.synthetic.main.activity_main.btAnswer4
import kotlinx.android.synthetic.main.activity_main.btAnswer5
import kotlinx.android.synthetic.main.activity_main.btAnswer6
import kotlinx.android.synthetic.main.activity_main.btAnswer7
import kotlinx.android.synthetic.main.activity_main.btAnswer8
import kotlinx.android.synthetic.main.activity_main.btAnswer9
import kotlinx.android.synthetic.main.activity_main.section1
import kotlinx.android.synthetic.main.activity_main.section2
import kotlinx.android.synthetic.main.activity_main.section3
import kotlinx.android.synthetic.main.activity_main.section4
import kotlinx.android.synthetic.main.activity_main.section5
import kotlinx.android.synthetic.main.activity_main.section6
import kotlinx.android.synthetic.main.activity_main.section7
import kotlinx.android.synthetic.main.activity_main.section8
import kotlinx.android.synthetic.main.activity_main.section9
import kotlinx.android.synthetic.main.sudoku_cell_section.view.*
import javax.inject.Inject

class CreateTableActivity : AppCompatActivity() {
    private var onFocusButton = BooleanArray(9) { false }
    lateinit var viewCells: Array<Array<View>>
    private lateinit var tableType: TableType
    private lateinit var viewModel: CreateViewModel

    @Inject
    lateinit var factory: CreateViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_table)
        component.inject(this)

        tableType = if (intent.getStringExtra("tableType").isNullOrEmpty()) {
            TableType.NORMAL
        } else {
            TableType.valueOf(intent.getStringExtra("tableType")!!)
        }

        viewModel = ViewModelProviders.of(this, factory).get(CreateViewModel::class.java)

        viewCells = arrayOf(
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


    }

    private fun setBackgroundNormal() {
        viewCells.forEachCellIndexed { rowIndex, columnIndex, cellView ->
            cellView.setBackgroundResource(R.drawable.cell_border)
            when (tableType) {
                TableType.DIAGONAL -> {
                    if (rowIndex == columnIndex || rowIndex + columnIndex == 8)
                        cellView.setBackgroundResource(R.drawable.diagonal_cell_background)
                }
                else -> {}
            }
        }
    }

    private fun initializeTableCellsListeners() {

        viewCells.forEachCellIndexed { rowIndex, columnIndex, cellView ->
            cellView.setOnClickListener {
                if (viewModel.isDelete()) {
                    onFocusButton.fill(false)
                    viewModel.hideNumber(rowIndex, columnIndex)
                } else if (onFocusButton.contains(true)) {
                    viewModel.writeAnswer(
                        Pair(rowIndex, columnIndex),
                        onFocusButton.indexOf(true) + 1
                    )
                }

                setBackgroundNormal()
                for (i in 0 until 9) {
                    viewCells[rowIndex, i].setBackgroundResource(R.drawable.selected_row_or_column_cell)
                    viewCells[i, columnIndex].setBackgroundResource(R.drawable.selected_row_or_column_cell)

                }
                cellView.setBackgroundResource(R.drawable.cell_border)

                when (tableType) {
                    TableType.DIAGONAL -> {
                        if (rowIndex == columnIndex || rowIndex + columnIndex == 8)
                            cellView.setBackgroundResource(R.drawable.diagonal_cell_background)
                    }
                    else -> {}
                }
            }
            when (tableType) {
                TableType.DIAGONAL -> {
                    if (rowIndex == columnIndex || rowIndex + columnIndex == 8)
                        cellView.setBackgroundResource(R.drawable.diagonal_cell_background)
                }
                else -> {}
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

        btBack.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MainMenuActivity::class.java
                )
            )
        }
        btSave.setOnClickListener {
            viewModel.saveTable()
        }
    }

}