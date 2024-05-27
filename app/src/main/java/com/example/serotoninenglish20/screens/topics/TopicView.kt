package com.example.serotoninenglish20.screens.topics

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.serotoninenglish20.R


@Composable
fun TopicsView(topicViewModel: TopicViewModel, onClick: (path: String) -> Unit) {
    val topics by topicViewModel.topics.observeAsState()
    LaunchedEffect(Unit) {
        topicViewModel.fetchTopics()
    }
    if (topics.isNullOrEmpty()) {
        return
    }
    LazyColumn(
        contentPadding = PaddingValues(
            top = dimensionResource(id = R.dimen.padding_large),
            bottom = dimensionResource(id = R.dimen.padding_large)
        )
    ) {
        items(topics as List<Topic>) {
            TopicCard(
                title = it.title,
                topicList = it.child,
            ) {
                onClick(it)
            }
        }
    }
}

@Composable
fun TopicCard(
    title: String,
    topicList: List<SubTopic>,
    onClick: (path: String) -> Unit
) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier
            .padding(
                start = dimensionResource(R.dimen.padding_large),
                bottom = dimensionResource(R.dimen.padding_small)
            )
    )
    Cards(
        topicList = topicList,
        modifier = Modifier
            .animateContentSize { initialValue, targetValue -> }
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_small))
    ) {
        onClick(it)
    }
}

@Composable
fun Cards(topicList: List<SubTopic>, modifier: Modifier = Modifier,  onClick: (path: String) -> Unit) {

    topicList.forEach { cardItems ->
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            ),
            modifier = modifier
                .clickable {
                    onClick(cardItems.path)
                }
        ) {
            var expanded by rememberSaveable {
                mutableStateOf(false)
            }
            Text(
                text = cardItems.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(
                        start = dimensionResource(R.dimen.padding_medium),
                        top = dimensionResource(R.dimen.padding_medium)
                    )
            )

            Text(
                text = cardItems.description,
                maxLines = if (expanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(
                        start = dimensionResource(R.dimen.padding_medium),
                        end = dimensionResource(R.dimen.padding_medium)
                    )
            )
            TextButton(
                onClick = { expanded = !expanded },
            ) {
                Text(text = if (expanded) "Свернуть" else "Подробнее" )
            }
        }
    }
}
