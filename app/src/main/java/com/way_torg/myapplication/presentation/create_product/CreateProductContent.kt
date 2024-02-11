package com.way_torg.myapplication.presentation.create_product

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.way_torg.myapplication.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProductContent(
    component: CreateProductComponent
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.create_product)) },
                navigationIcon = {
                    IconButton(onClick = {
                        component.onClickBack()
                    }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(state = rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedTextField(
                    value = "",
                    placeholder = { Text(stringResource(R.string.name)) },
                    onValueChange = {}
                )
                OutlinedTextField(
                    value = "",
                    placeholder = { Text(stringResource(R.string.category)) },
                    onValueChange = {}
                )
                OutlinedTextField(
                    value = "",
                    placeholder = { Text(stringResource(R.string.description)) },
                    onValueChange = {},
                    modifier = Modifier.height(150.dp)
                )
                OutlinedTextField(
                    value = "",
                    placeholder = { Text(stringResource(R.string.count)) },
                    onValueChange = {}
                )
                OutlinedTextField(
                    value = "",
                    placeholder = { Text(stringResource(R.string.price)) },
                    onValueChange = {}
                )
                OutlinedTextField(
                    value = "",
                    placeholder = { Text(stringResource(R.string.discount)) },
                    onValueChange = {}
                )
                Box(
                    modifier = Modifier
                        .border(
                            border = BorderStroke(width = 1.dp, color = Color.Black),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .size(width = TextFieldDefaults.MinWidth, height = 100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Image, contentDescription = null)
                }
                Spacer(Modifier.weight(1f))
                OutlinedButton(
                    onClick = {},
                ) {
                    Text(stringResource(R.string.create), color = Color.Black)
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}