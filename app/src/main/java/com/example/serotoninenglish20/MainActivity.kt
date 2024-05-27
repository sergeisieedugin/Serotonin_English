package com.example.serotoninenglish20

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.serotoninenglish20.screens.guess.GuessView
import com.example.serotoninenglish20.screens.guess.GuessViewModel
import com.example.serotoninenglish20.screens.topics.TopicViewModel
import com.example.serotoninenglish20.screens.topics.TopicsView


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val topicViewModel: TopicViewModel by viewModels()
        val guessViewModel: GuessViewModel by viewModels()

        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "topics_view"
                    ){
                        composable("topics_view"){
                            TopicsView(topicViewModel = topicViewModel){
                                guessViewModel.themePath = it
                                guessViewModel.fetchSentence()
                                navController.navigate("guess_view")
                            }
                        }
                        composable("guess_view"){
                            GuessView(guessViewModel){
                                navController.navigate("topics_view")
                            }
                        }
                    }
                }
            }
        }
    }
}
