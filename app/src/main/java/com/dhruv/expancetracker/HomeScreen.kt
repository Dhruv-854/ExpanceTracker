package com.dhruv.expancetracker

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.dhruv.expancetracker.data.model.ExpenseEntity
import com.dhruv.expancetracker.ui.theme.GreenCard
import com.dhruv.expancetracker.viewModel.HomeViewModel
import com.dhruv.expancetracker.viewModel.HomeViewModelFactory

@Composable
fun HomeScreen(modifier: Modifier = Modifier, navController: NavHostController) {

    val viewModel: HomeViewModel =
        HomeViewModelFactory(LocalContext.current).create(HomeViewModel::class.java)

    Surface(
        modifier.fillMaxSize()
    ) {
        ConstraintLayout(modifier.fillMaxSize()) {
            val (
                nameRow, list, card, topBar, add,
            ) = createRefs()
            Image(
                painter = painterResource(id = R.drawable.topbar),
                contentDescription = null
            )
            Box(
                modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp, start = 16.dp, end = 16.dp)
                    .constrainAs(nameRow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Column {
                    Text(
                        text = "Good Morning...", fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(text = "We wish you have a good day", fontSize = 16.sp)
                }
                Image(
                    painter = painterResource(id = R.drawable.notification),
                    contentDescription = null,
                    modifier.align(Alignment.CenterEnd)
                )
            }

            val state = viewModel.expenses.collectAsState(initial = emptyList())
            val expenses = viewModel.getTotalExpenses(state.value)
            val income = viewModel.getTotalIncome(state.value)
            val balance = viewModel.getBalance(state.value)


            CardItem(
                modifier
                    .constrainAs(card) {
                        top.linkTo(nameRow.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                expenses, income, balance
            )
            TransactionList(
                modifier
                    .fillMaxWidth()
                    .constrainAs(list) {
                        top.linkTo(card.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    },
                list = state.value, viewModel
            )
            Image(
                imageVector = Icons.Default.Add, contentDescription = null,
                modifier = modifier
                    .constrainAs(add) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable {
                        navController.navigate("/addExpense")
                    }
            )
        }
    }
}


@Composable
fun CardItem(modifier: Modifier = Modifier, expenses: String, income: String, balance: String) {
    Column(
        modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(200.dp)
            .clip(shape = RoundedCornerShape(16.dp))
            .background(GreenCard)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Row(
                modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(
                ) {

                    Text(
                        text = "Total Balance",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Text(
                        text = balance,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.dot_menu),
                    contentDescription = null,
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CardRowItem(title = "Income", amount = income, image = R.drawable.income)
            CardRowItem(title = "Expanse", amount = expenses, image = R.drawable.expance)
        }
    }
}

@Composable
fun CardRowItem(title: String, amount: String, image: Int, modifier: Modifier = Modifier) {
    Column(
    ) {
        Row {
            Image(painter = painterResource(id = image), contentDescription = null)
            Spacer(modifier.size(8.dp))
            Text(text = title, fontSize = 16.sp, color = Color.White)
        }
        Text(text = amount, fontSize = 20.sp, color = Color.White)
    }
}

@Composable
fun TransactionList(
    modifier: Modifier = Modifier, list: List<ExpenseEntity>,
    viewModel: HomeViewModel,
) {
    LazyColumn(
        modifier.padding(horizontal = 16.dp)
    ) {
        item {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Transactions", fontSize = 20.sp)
                Text(
                    text = "See All",
                    fontSize = 16.sp,
                    color = GreenCard,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
        items(list) { item ->
            TransactionItem(
                title = item.title,
                amount = item.amount.toString(),
                icon = viewModel.getItemIcon(item),
                date = item.date,
                color = if (item.type == "Income") Color.Green else Color.Red,
                modifier = Modifier.clickable {
//
//                    )
                }
            )

        }
    }
}

@Composable
fun TransactionItem(
    modifier: Modifier = Modifier,
    title: String,
    amount: String,
    date: String,
    icon: Int,
    color: Color,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Row {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = modifier.size(50.dp)
            )
            Spacer(modifier.size(8.dp))
            Column {
                Text(text = title, fontSize = 16.sp)
                Spacer(modifier.size(8.dp))
                Text(text = date, fontSize = 12.sp)
            }
        }
        Text(
            text = "$${amount}", fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterEnd),
            color = color
        )
        Spacer(modifier.size(8.dp))
    }
}


@SuppressLint("ComposableNaming")
@Composable
fun clickDialog(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    onConfirm: () -> Unit,
    ) {
    AlertDialog(
        onDismissRequest = {
            onDelete.invoke()
        }, confirmButton = {
            onConfirm.invoke()
        }
    )
}










