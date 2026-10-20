package com.lifequest.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lifequest.app.ui.theme.LqColors

@Composable
fun DevConsoleDialog(
    onDismiss: () -> Unit,
    onCheatXp: () -> Unit,
    onCheatGold: () -> Unit,
    onKillBoss: () -> Unit,
    onReset: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Developer Console") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onCheatXp, modifier = Modifier.fillMaxWidth()) { Text("+100 XP") }
                Button(onClick = onCheatGold, modifier = Modifier.fillMaxWidth()) { Text("+1000 Gold") }
                Button(onClick = onKillBoss, modifier = Modifier.fillMaxWidth()) { Text("Kill Boss") }
                Button(
                    onClick = { onReset(); onDismiss() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = LqColors.Danger),
                ) { Text("Reset Save") }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Đóng") } },
    )
}
