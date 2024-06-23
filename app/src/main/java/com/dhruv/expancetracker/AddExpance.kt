package com.dhruv.expancetracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.dhruv.expancetracker.data.model.ExpenseEntity
import com.dhruv.expancetracker.viewModel.AddExpenseViewModel
import com.dhruv.expancetracker.viewModel.AddExpenseViewModelFactory
import kotlinx.coroutines.launch

//@Preview(showBackground = true)
@Composable
fun AddExpance(modifier : Modifier , navController: NavHostController) {

    val viewModel =
        AddExpenseViewModelFactory(LocalContext.current).create(AddExpenseViewModel::class.java)
    val scope = rememberCoroutineScope()

    Surface(
        modifier = modifier.fillMaxSize(),
    ) {
        ConstraintLayout(modifier = modifier.fillMaxSize()) {

            val (nameRow, list, card, topBar) = createRefs()
            Image(painter = painterResource(id = R.drawable.topbar),
                contentDescription = null,
                modifier = modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            Box(modifier = modifier
                .fillMaxWidth()
                .padding(top = 60.dp, start = 16.dp, end = 16.dp)
                .constrainAs(nameRow) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                Image(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = null,
                    modifier = modifier.align(Alignment.CenterStart).clickable {
                        navController.popBackStack()
                    }
                )
                Text(
                    text = "Add Expance",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier.align(Alignment.Center)
                )
                Image(
                    painter = painterResource(id = R.drawable.dot_menu),
                    contentDescription = null,
                    modifier = modifier.align(Alignment.CenterEnd)
                )
            }
            DataForm(modifier = modifier
                .constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(top = 45.dp)
            ){
                scope.launch {
                    if (
                        viewModel.addExpense(it)
                    ){
                        navController.popBackStack()
                    }

                }
            }
        }
    }
}

@Composable
fun DataForm(
    modifier: Modifier = Modifier,
    onAddExpenseClick: (model: ExpenseEntity) -> Unit,
) {


    val name = remember {
        mutableStateOf("")
    }
    val amount = remember {
        mutableStateOf("")
    }
    val date = remember {
        mutableStateOf(0L)
    }
    val dateDialogVisibility = remember {
        mutableStateOf(false)
    }
    val category = remember {
        mutableStateOf("")
    }
    val type = remember {
        mutableStateOf("")
    }

    Box(modifier = modifier.fillMaxSize()) {

        Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(16.dp))
//                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = "Name", fontSize = 14.sp, color = Color.Gray
            )
            Spacer(modifier = Modifier.size(4.dp))
            OutlinedTextField(
                value = name.value,
                onValueChange = { name.value = it },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.size(16.dp))

            Text(
                text = "Amount", fontSize = 14.sp, color = Color.Gray

            )
            Spacer(modifier = Modifier.size(4.dp))

            OutlinedTextField(
                value = amount.value, onValueChange = {
                    amount.value = it
                }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.size(16.dp))

            Text(
                text = "Date", fontSize = 14.sp, color = Color.Gray
            )
            Spacer(modifier = Modifier.size(4.dp))
            OutlinedTextField(
                value = if (date.value == 0L) "Select Date" else (Utils.formatDateToHumanReadableForm(
                    date.value
                )),
                onValueChange = {

                }, modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        dateDialogVisibility.value = true
                    },
                enabled = false
            )
            Spacer(modifier = Modifier.size(16.dp))

            Text(
                text = "Category", fontSize = 14.sp, color = Color.Gray
            )
            Spacer(modifier = Modifier.size(4.dp))

            ExpenseDropDown(
                listOfItems = listOf(
                    "Netflix", "Upwork", "Starbucks",
                    "Paypal",
                    "Salary"
                ),
                onItemSelected = {
                    category.value = it
                }
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = "Type", fontSize = 14.sp, color = Color.Gray
            )
            Spacer(modifier = Modifier.size(4.dp))

            ExpenseDropDown(
                listOfItems = listOf(
                    "Expense",
                    "Income"
                ),
                onItemSelected = {
                    type.value = it
                }
            )
            Spacer(modifier = Modifier.size(16.dp))


            Button(
                onClick = {
                    val model = ExpenseEntity(
                        id = null,
                        title = name.value,
                        amount = amount.value.toDoubleOrNull() ?: 0.0,
                        date = Utils.formatDateToHumanReadableForm(date.value),
                        category = category.value,
                        type = type.value

                    )
                    onAddExpenseClick.invoke(model)
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Add Expense")
            }

        }
        if (dateDialogVisibility.value) {
            ExpenseDatePickerDialog(
                onDateSelected = {
                    date.value = it
                    dateDialogVisibility.value = false
                },
                onDismiss = {
                    dateDialogVisibility.value = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDropDown(
    modifier: Modifier = Modifier,
    listOfItems: List<String>,
    onItemSelected: (item: String) -> Unit,
) {
    val expanded = remember {
        mutableStateOf(false)
    }
    val selectedItem = remember {
        mutableStateOf(listOfItems[0])
    }

    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = {
            expanded.value = !expanded.value
        }
    ) {
        TextField(
            value = selectedItem.value,
            onValueChange = {
                selectedItem.value = it
            },
            modifier = modifier
                .fillMaxWidth()
                .menuAnchor()
                .focusRequester(FocusRequester()),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
            },
            enabled = false
        )
        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            }) {

            listOfItems.forEach {
                DropdownMenuItem(text = {
                    Text(text = it)
                }, onClick = {
                    selectedItem.value = it
                    onItemSelected.invoke(selectedItem.value)
                    expanded.value = !expanded.value
                })
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDatePickerDialog(
    modifier: Modifier = Modifier,
    onDateSelected: (date: Long) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis ?: 0L
    DatePickerDialog(
        onDismissRequest = {
            onDismiss.invoke()
        }, confirmButton = {
            TextButton(onClick = {
                onDateSelected.invoke(selectedDate)

            }) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDateSelected.invoke(selectedDate)
            }) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

