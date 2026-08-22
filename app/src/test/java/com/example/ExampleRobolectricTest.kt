package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.AnimeCharacter
import com.example.data.model.GamePhase
import com.example.data.model.GameSettings
import com.example.data.model.Player
import com.example.engine.ClueValidator
import com.example.engine.GameEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Anime Imposter", appName)
  }

  @Test
  fun `game engine initializes and assigns imposter correctly`() {
    val engine = GameEngine()
    val host = Player(id = "p1", username = "Kage", avatarId = "avatar_1", isHost = true)
    engine.createRoom(host, GameSettings())
    engine.fillWithBotsUpTo(6)

    assertEquals(6, engine.state.players.size)

    val testCharacters = listOf(
      AnimeCharacter(
        id = "c1",
        name = "Naruto Uzumaki",
        anime = "Naruto",
        category = "Shonen",
        difficulty = "Easy",
        traits = listOf("Ninja", "Orange outfit"),
        abilities = listOf("Rasengan", "Shadow Clone")
      ),
      AnimeCharacter(
        id = "c2",
        name = "Monkey D. Luffy",
        anime = "One Piece",
        category = "Shonen",
        difficulty = "Easy",
        traits = listOf("Straw Hat", "Pirate"),
        abilities = listOf("Gum-Gum Pistol", "Gear 5")
      )
    )

    val (started, _) = engine.startGame(testCharacters)
    assertTrue(started)
    assertEquals(GamePhase.ROLE_REVEAL, engine.state.phase)
    assertNotNull(engine.state.imposterPlayerId)

    // Verify sanitization for Imposter client
    val imposterId = engine.state.imposterPlayerId
    val imposterState = engine.sanitizeStateForPlayer(imposterId)
    assertEquals(null, imposterState.character) // character secret hidden from imposter!

    // Verify normal player receives character
    val normalPlayerId = engine.state.players.first { it.id != imposterId }.id
    val normalState = engine.sanitizeStateForPlayer(normalPlayerId)
    assertNotNull(normalState.character)
  }

  @Test
  fun `clue validator rejects spoilers and permits valid clues`() {
    val char = AnimeCharacter(
      id = "c1",
      name = "Satoru Gojo",
      anime = "Jujutsu Kaisen",
      category = "Shonen",
      difficulty = "Medium",
      traits = listOf("Blindfold"),
      abilities = listOf("Hollow Purple")
    )

    // Direct spoiler in clue
    val result1 = ClueValidator.validateClue("He is Gojo the strongest sorcerer", char)
    assertTrue(result1 is ClueValidator.ValidationResult.Rejected)

    // Valid subtle clue
    val result2 = ClueValidator.validateClue("He covers his eyes and has white hair", char)
    assertTrue(result2 is ClueValidator.ValidationResult.Valid)
  }
}
