package com.example.serotoninenglish20.screens.guess

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.serotoninenglish20.R
import kotlinx.coroutines.launch


@Composable
fun GuessView(guessViewModel: GuessViewModel = viewModel()) {
    val guessSentence by guessViewModel.guessItems.observeAsState()

    var showBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }

    var isAnswerValid by rememberSaveable {
        mutableStateOf(false)
    }

    var showValidBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }

    val chosenWords = remember {
        guessViewModel.chosenWords
    }
    val words = remember {
        guessViewModel.words
    }


    Scaffold(
        topBar = {
            TopBar()
        }
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(dimensionResource(R.dimen.padding_large))
                    ) {
                        Text(
                            text = "Present simple",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier
                                .padding(end = dimensionResource(R.dimen.padding_small))
                        )
                        Icon(
                            imageVector = Icons.Default.HelpOutline,
                            contentDescription = null,
                            modifier = Modifier
                                .clickable {
                                    showBottomSheet = true
                                })
                    }

                    Text(
                        text = stringResource(id = R.string.translate),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(
                    modifier = Modifier
                        .size(32.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 100.dp)
                        .border(BorderStroke(1.dp, Color.Gray), RoundedCornerShape(8.dp))
                        .padding(dimensionResource(R.dimen.padding_medium))
                ) {
                    guessSentence?.let {
                        Text(
                            text = it.russianPhrase
                        )
                    }
                    Chips(chosenWords, Arrangement.Start) {
                        guessViewModel.deleteFromChosen(it)
                    }
                }
                Spacer(modifier = Modifier.fillMaxHeight(0.5F))
                Column(
                    modifier = Modifier
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Chips(words, Arrangement.Center) {
                        guessViewModel.toChosenWords(it)
                    }
                    Button(
                        onClick = {
                            guessViewModel.viewModelScope.launch {
                                isAnswerValid = guessViewModel.checkAnswer()
                                showValidBottomSheet = true
                            }
                        },
                        enabled = if (chosenWords.isEmpty()) false else true,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.button_check),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
            InfoBottomSheet(guessViewModel, showBottomSheet) {
                showBottomSheet = !showBottomSheet
            }
           ValidBottomSheet(isAnswerValid = isAnswerValid, showValidBottomSheet = showValidBottomSheet,
               {guessViewModel.fetchSentence()},
               {showValidBottomSheet = !showValidBottomSheet}
               )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon = {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
        },
        title = {
            Text("Present simple")
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoBottomSheet(guessViewModel: GuessViewModel, showBottomSheet: Boolean, onClose: () -> Unit) {
    val description by guessViewModel.sentenceDescription.observeAsState()
    val sheetState = rememberModalBottomSheetState()

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { onClose() },
            sheetState = sheetState,
            windowInsets = WindowInsets.navigationBars
        ) {
            LaunchedEffect(Unit) {
                guessViewModel.fetchDescription()
            }
            description?.theme?.let {
                Text(
                    text = it.description,
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.padding_small))
                )
            }
            description?.theme?.let {
                Text(
                    text = it.sentenceFormula,
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.padding_small))
                        .border(BorderStroke(1.dp, Color.Gray), RoundedCornerShape(4.dp))
                        .padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 4.dp)
                )
            }
            description?.theme?.let {
                Text(
                    text = it.questionFormula,
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.padding_small))
                        .border(BorderStroke(1.dp, Color.Gray), RoundedCornerShape(4.dp))
                        .padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 4.dp)
                )
            }
            Text(
                text = "Примеры:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
            )
            description?.theme?.examples?.forEach {
                Text(
                    text = it,
                    modifier = Modifier
                        .padding(
                            start = dimensionResource(id = R.dimen.padding_small),
                            end = dimensionResource(id = R.dimen.padding_small),
                            bottom = dimensionResource(id = R.dimen.padding_medium)
                        )
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidBottomSheet(isAnswerValid: Boolean?, showValidBottomSheet: Boolean, getSentence: () -> Unit, onClose: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    if (showValidBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { onClose() },
            sheetState = sheetState,
            windowInsets = WindowInsets.navigationBars,
            dragHandle = null
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(id = R.dimen.padding_large),
                        end = dimensionResource(id = R.dimen.padding_large),
                        bottom = dimensionResource(id = R.dimen.padding_large),
                        top = 32.dp
                    )
            ) {
                if (isAnswerValid == false){
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.error_24px),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .size(42.dp)
                                .padding(end = dimensionResource(id = R.dimen.padding_small))
                        )
                        Text(
                            text = "Не верно",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                    Column {
                        Text(
                            text = stringResource(id = R.string.answer),
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier
                                .padding(
                                    top = dimensionResource(id = R.dimen.padding_medium)
                                )
                        )
                        Text(
                            text = "He is at home now",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    Button(
                        onClick = {
                            getSentence()
                            onClose()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = dimensionResource(id = R.dimen.padding_large)
                            )
                    ) {
                        Text(
                            text = stringResource(id = R.string.continue_button),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                } else{
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.check_circle_24px),
                            contentDescription = null,
                            tint = Color(0xFF67B90D),
                            modifier = Modifier
                                .size(42.dp)
                                .padding(end = dimensionResource(id = R.dimen.padding_small))
                        )
                        Text(
                            text = "Верно",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                    Button(
                        onClick = {
                            getSentence()
                            onClose()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF67B90D)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = dimensionResource(id = R.dimen.padding_large)
                            )
                    ) {
                        Text(
                            text = stringResource(id = R.string.continue_button),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Chips(
    words: List<String>,
    arrangement: Arrangement.Horizontal,
    clickable: (index: Int) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    FlowRow(
        horizontalArrangement = arrangement
    ) {
        words.forEachIndexed { index, word ->
            Text(
                text = word,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(
                        end = dimensionResource(id = R.dimen.padding_small),
                        top = 12.dp
                    )
                    .border(BorderStroke(1.dp, Color.Gray), RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(8.dp))
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        clickable(index)
                    }
                    .padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 4.dp)
            )
        }
    }
}


@Preview
@Composable
fun guessPreview() {
    GuessView(guessViewModel = GuessViewModel())
}

