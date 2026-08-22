package com.example.data.repository

import com.example.data.database.dao.CharacterDao
import com.example.data.database.entities.CharacterEntity
import com.example.data.model.AnimeCharacter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CharacterRepository(private val characterDao: CharacterDao) {

    val allCharacters: Flow<List<AnimeCharacter>> = characterDao.getAllCharacters().map { entities ->
        entities.map { it.toDomain() }
    }

    suspend fun getCharacterCount(): Int = characterDao.getCharacterCount()

    suspend fun initializePresetCharactersIfNeeded() {
        if (characterDao.getCharacterCount() == 0) {
            val entities = PRESET_CHARACTERS.map { it.toEntity() }
            characterDao.insertAll(entities)
        }
    }

    suspend fun addCharacter(character: AnimeCharacter) {
        characterDao.insert(character.toEntity())
    }

    suspend fun deleteCharacter(id: String) {
        characterDao.deleteCharacter(id)
    }

    suspend fun getCharacterById(id: String): AnimeCharacter? {
        return characterDao.getCharacterById(id)?.toDomain()
    }

    companion object {
        val PRESET_CHARACTERS = listOf(
            AnimeCharacter(
                id = "char_naruto",
                name = "Naruto Uzumaki",
                anime = "Naruto: Shippuden",
                aliases = listOf("Seventh Hokage", "Hero of the Hidden Leaf", "Nine-Tails Jinchuriki"),
                difficulty = "Easy",
                category = "Shonen",
                traits = listOf("Never gives up", "Loves ramen", "Wants to be Hokage", "Orange tracksuit", "Shadow clones"),
                abilities = listOf("Rasengan", "Shadow Clone Jutsu", "Sage Mode", "Nine-Tails Chakra"),
                famousQuote = "I never go back on my word! That's my nindo: my ninja way!",
                appearance = "Spiky blond hair, blue eyes, whisker marks on cheeks",
                spoilerSafeHints = listOf("He has a golden-haired spirit locked within him", "Always visits Ichiraku after missions", "Believes strongly in the power of bonds")
            ),
            AnimeCharacter(
                id = "char_luffy",
                name = "Monkey D. Luffy",
                anime = "One Piece",
                aliases = listOf("Straw Hat Luffy", "Emperor of the Sea", "Joy Boy"),
                difficulty = "Easy",
                category = "Shonen",
                traits = listOf("Wants to be King of the Pirates", "Loves meat", "Elastic rubber body", "Straw hat treasure", "Fearless navigator"),
                abilities = listOf("Gum-Gum Pistol", "Gear 5", "Conqueror's Haki", "Sun God Nika"),
                famousQuote = "If you don't take risks, you can't create a future!",
                appearance = "Straw hat, red vest, scar under left eye, rubber limbs",
                spoilerSafeHints = listOf("Cannot swim because of a fruit he ate as a child", "Gave a solemn promise to a red-haired mentor", "Recruits a reindeer doctor and a skeleton musician")
            ),
            AnimeCharacter(
                id = "char_goku",
                name = "Son Goku",
                anime = "Dragon Ball Z",
                aliases = listOf("Kakarot", "Super Saiyan of Legend", "Defender of Earth"),
                difficulty = "Easy",
                category = "Shonen",
                traits = listOf("Saiyan warrior", "Craves strong opponents", "Pure of heart", "Endless appetite", "Instant transmission"),
                abilities = listOf("Kamehameha", "Spirit Bomb", "Ultra Instinct", "Kaioken"),
                famousQuote = "I am the hope of the universe. I am the answer to all living things that cry out for peace.",
                appearance = "Wild spiky black hair that turns golden, orange martial arts gi",
                spoilerSafeHints = listOf("Traveled on a yellow flying cloud", "Collected seven glowing orange spheres", "Raised on Mount Paozu by his grandfather")
            ),
            AnimeCharacter(
                id = "char_levi",
                name = "Levi Ackerman",
                anime = "Attack on Titan",
                aliases = listOf("Humanity's Strongest Soldier", "Captain Levi", "Clean Freak"),
                difficulty = "Medium",
                category = "Action",
                traits = listOf("Obsessed with cleanliness", "Incredible agility", "Holds tea cups unusually", "Scout Regiment leader", "Stoic exterior"),
                abilities = listOf("Omni-Directional Mobility gear mastery", "Spinning blade attack", "Ackerman awakened power"),
                famousQuote = "The only thing we're allowed to do is to believe that we won't regret the choice we made.",
                appearance = "Short undercut black hair, green Scout cape, cravat collar",
                spoilerSafeHints = listOf("Extremely particular about dusting every corner", "Spun like a whirlwind through the Beast in the forest", "Born beneath the capital in the underground city")
            ),
            AnimeCharacter(
                id = "char_gojo",
                name = "Satoru Gojo",
                anime = "Jujutsu Kaisen",
                aliases = listOf("The Strongest Sorcerer", "Six Eyes Bearer", "Honored One"),
                difficulty = "Easy",
                category = "Action",
                traits = listOf("Overwhelming confidence", "Sweet tooth", "Blindfolded mentor", "Limitless manipulation", "Protects young sorcerers"),
                abilities = listOf("Limitless", "Six Eyes", "Hollow Purple", "Infinite Void Domain Expansion"),
                famousQuote = "Throughout heaven and earth, I alone am the honored one.",
                appearance = "Tall with spiky snow-white hair, black blindfold, glowing azure eyes",
                spoilerSafeHints = listOf("Infinity separates him from anything trying to touch him", "Loves buying gourmet desserts across Tokyo", "Teacher at Tokyo Jujutsu High")
            ),
            AnimeCharacter(
                id = "char_light",
                name = "Light Yagami",
                anime = "Death Note",
                aliases = listOf("Kira", "God of the New World", "Second L"),
                difficulty = "Medium",
                category = "Psychological",
                traits = listOf("Genius student", "God complex", "Eating potato chips dramatically", "Manipulative mastermind", "Wants a crime-free world"),
                abilities = listOf("Death Note rules mastery", "Genius intellect", "Shinigami coordination", "Psychological manipulation"),
                famousQuote = "I'll take a potato chip... and eat it!",
                appearance = "Neat brown hair, school blazer, crimson inner gaze when scheming",
                spoilerSafeHints = listOf("Accompanied by an apple-loving entity with wings", "Engaged in a game of tennis with his greatest rival", "Wrote names furiously in a dark leather notebook")
            ),
            AnimeCharacter(
                id = "char_lelouch",
                name = "Lelouch vi Britannia",
                anime = "Code Geass",
                aliases = listOf("Zero", "The Demon Emperor", "Black Knights Commander"),
                difficulty = "Medium",
                category = "Psychological",
                traits = listOf("Chess prodigy", "Wears a masked cape", "Fights for his disabled sister", "Rebellion leader", "Master tactician"),
                abilities = listOf("Geass: Power of Absolute Obedience", "Knightmare Frame piloting", "Strategic brilliance"),
                famousQuote = "The only ones who should kill are those who are prepared to be killed.",
                appearance = "Dark hair, slender build, regal demeanor, glowing crimson bird sigil in eye",
                spoilerSafeHints = listOf("Formed an alliance with a green-haired immortal", "Lead the Order of the Black Knights", "Sacrificed himself in a zero requiem")
            ),
            AnimeCharacter(
                id = "char_tanjirou",
                name = "Tanjiro Kamado",
                anime = "Demon Slayer",
                aliases = listOf("Water Breathing Swordsman", "Sun Breather Heir", "Kind-Hearted Slayer"),
                difficulty = "Easy",
                category = "Shonen",
                traits = listOf("Incredible sense of smell", "Hard forehead", "Carries wooden box", "Kind to demons", "Hanafuda earrings"),
                abilities = listOf("Water Breathing", "Hinokami Kagura / Sun Breathing", "Transparent World"),
                famousQuote = "No matter how many people you may lose, you have no choice but to go on living.",
                appearance = "Burgundy hair with scar on forehead, checkered green and black haori",
                spoilerSafeHints = listOf("Travels with his demonized sister in a box on his back", "Trained on Mount Sagiri slicing a giant boulder", "Wears heirloom hanafuda earrings")
            ),
            AnimeCharacter(
                id = "char_rimuru",
                name = "Rimuru Tempest",
                anime = "That Time I Got Reincarnated as a Slime",
                aliases = listOf("Demon Lord Rimuru", "Ruler of Monsters", "Chaos Creator"),
                difficulty = "Medium",
                category = "Isekai",
                traits = listOf("Started as a slime", "Absorbs skills", "Diplomatic nation builder", "Friendly leader", "Great Sage assistant"),
                abilities = listOf("Predator / Gluttony", "Great Sage / Raphael", "Black Flame", "Megiddo"),
                famousQuote = "I'm not a bad slime, you know!",
                appearance = "Blue translucent slime blob or androgynous silver-blue haired youth",
                spoilerSafeHints = listOf("Reincarnated after saving a junior colleague on the street", "Befriended a sealed Storm Dragon in a cave", "Built the Jura Tempest Federation")
            ),
            AnimeCharacter(
                id = "char_subaru",
                name = "Subaru Natsuki",
                anime = "Re:Zero - Starting Life in Another World",
                aliases = listOf("Lolimancer", "Knight of Emilia", "Purveyor of Despair"),
                difficulty = "Medium",
                category = "Isekai",
                traits = listOf("Tracksuit wearer", "Trauma survivor", "Returns upon dying", "Loves silver-haired half-elf", "Never gives up despite despair"),
                abilities = listOf("Return by Death", "Invisible Providence", "Shamak magic", "Cor Leonis"),
                famousQuote = "I love Emilia.",
                appearance = "Short black hair, black/orange tracksuit, sunken eyes from sleepless loops",
                spoilerSafeHints = listOf("Summoned right after leaving a convenience store", "Remembers events from wiped timelines that nobody else does", "Has a foul scent noticed by witch beasts")
            ),
            AnimeCharacter(
                id = "char_edward",
                name = "Edward Elric",
                anime = "Fullmetal Alchemist: Brotherhood",
                aliases = listOf("Fullmetal Alchemist", "Hero of the People", "Shorty"),
                difficulty = "Easy",
                category = "Fantasy",
                traits = listOf("Hates milk", "Furious when called short", "Automail limbs", "Claps hands to transmute", "Searching for Philosopher's Stone"),
                abilities = listOf("Alchemical transmutation without a circle", "Martial arts", "Automail blade"),
                famousQuote = "A lesson without pain is meaningless. For you cannot gain something without sacrificing something else in return.",
                appearance = "Braided blonde hair, red long coat, steel automail right arm and left leg",
                spoilerSafeHints = listOf("Committed the ultimate taboo to bring back their mother", "Bound his brother's soul to a giant iron suit of armor", "State Alchemist with a silver pocket watch")
            ),
            AnimeCharacter(
                id = "char_deku",
                name = "Izuku Midoriya",
                anime = "My Hero Academia",
                aliases = listOf("Deku", "Ninth One For All Successor", "Green Defend"),
                difficulty = "Easy",
                category = "Shonen",
                traits = listOf("Hero analysis notebook", "Broke bones initially", "Crybaby who grew brave", "Successor to Symbol of Peace", "Green lightning sparks"),
                abilities = listOf("One For All Full Cowl", "Blackwhip", "Float", "Danger Sense", "Smash"),
                famousQuote = "I have to work harder than anyone else to make it! I'll never catch up otherwise!",
                appearance = "Messy dark green hair, freckles, green hero suit with iron soles",
                spoilerSafeHints = listOf("Born quirkless in a super-powered society", "Cleaned an entire junk-filled beach as training", "Inherited hair from the greatest American-styled hero")
            ),
            AnimeCharacter(
                id = "char_saitama",
                name = "Saitama",
                anime = "One Punch Man",
                aliases = listOf("Caped Baldy", "Hero for Fun", "One Punch Master"),
                difficulty = "Easy",
                category = "Action",
                traits = listOf("Bored with fights", "Obsessed with supermarket sales", "100 pushups daily regimen", "Lives in Z-City ghost town", "Cyborg disciple"),
                abilities = listOf("Normal Punch", "Consecutive Normal Punches", "Serious Punch", "Infinite physical stats"),
                famousQuote = "I'm just a guy who's a hero for fun.",
                appearance = "Completely bald head, yellow jumpsuit with white cape and red gloves",
                spoilerSafeHints = listOf("Lost all his hair from intense 3-year workout routine", "One-shotted a giant subterranean monster before morning coffee", "Genos takes notes on his daily mundane lifestyle")
            ),
            AnimeCharacter(
                id = "char_killua",
                name = "Killua Zoldyck",
                anime = "Hunter x Hunter",
                aliases = listOf("Lightning Assassin", "Prodigy of the Zoldyck Family", "Godspeed User"),
                difficulty = "Medium",
                category = "Shonen",
                traits = listOf("Former assassin", "Loves chocolate robots", "Immune to poison and electricity", "Uses yoyos in battle", "Best friends with Gon"),
                abilities = listOf("Godspeed", "Lightning Palm", "Thunderbolt", "Rhythm Echo", "Assassin Claws"),
                famousQuote = "If I ignore a friend I have the ability to help, wouldn't I be betraying him?",
                appearance = "Spiky silver hair, electric blue eyes, skater shorts and turtleneck",
                spoilerSafeHints = listOf("Raised in a mountain fortress guarded by a giant hound", "Removes an insidious needle planted in his forehead by brother", "Transmutes his aura into high-voltage lightning")
            ),
            AnimeCharacter(
                id = "char_sukuna",
                name = "Ryomen Sukuna",
                anime = "Jujutsu Kaisen",
                aliases = listOf("King of Curses", "Disgraced One", "Fallen Sorcerer"),
                difficulty = "Medium",
                category = "Villains",
                traits = listOf("Cruel and sadistic", "Eats cursed fingers", "Takes over a teenager's body", "Malevolent shrine", "Fire and slash mastery"),
                abilities = listOf("Dismantle", "Cleave", "Domain Expansion: Malevolent Shrine", "Furnace Flame"),
                famousQuote = "Know your place, fool.",
                appearance = "Pink spiky hair, dark cursed markings across face and body, four eyes when awakened",
                spoilerSafeHints = listOf("Split his soul into twenty indestructible mummified fingers", "Carved Shibuya in a sphere of instantaneous slicing", "Loves taking interest in a shadow-summoning student")
            ),
            AnimeCharacter(
                id = "char_eren",
                name = "Eren Yeager",
                anime = "Attack on Titan",
                aliases = listOf("Attack Titan", "Founding Titan", "Usurper"),
                difficulty = "Medium",
                category = "Action",
                traits = listOf("Obsessed with freedom", "Bites hand to transform", "Keeps moving forward", "Green eyes of fury", "Rumbling harbinger"),
                abilities = listOf("Attack Titan future memories", "War Hammer Titan hardening", "Founding Titan command"),
                famousQuote = "If you win, you live. If you lose, you die. If you don't fight, you can't win!",
                appearance = "Long brown hair tied back, dark coat, hardened determined expression",
                spoilerSafeHints = listOf("Watched his mother get devoured as Shiganshina fell", "Discovered the truth about humanity outside the walls from a basement", "Unleashed thousands of colossal titans across the sea")
            ),
            AnimeCharacter(
                id = "char_kaguya",
                name = "Kaguya Shinomiya",
                anime = "Kaguya-sama: Love Is War",
                aliases = listOf("Vice President", "Ice Princess", "Heiress of Shinomiya Conglomerate"),
                difficulty = "Medium",
                category = "Romance",
                traits = listOf("Proud heiress", "Calculates romance like chess", "Wants president to confess first", "Rich family upbringing", "Loyal to friends"),
                abilities = listOf("Genius intellect", "Archery mastery", "Psychological mind games", "O Kawaii Koto reaction"),
                famousQuote = "How cute...",
                appearance = "Long black hair with red ribbon, Shuchiin academy uniform, ruby red eyes",
                spoilerSafeHints = listOf("Lives in a colossal mansion guarded by butler Hayasaka", "Refuses to confess love first believing it makes one the loser", "Panics whenever fireworks or smartphone pictures are mentioned")
            ),
            AnimeCharacter(
                id = "char_yor",
                name = "Yor Forger",
                anime = "Spy x Family",
                aliases = listOf("Thorn Princess", "Mama Yor", "Assassin of Garden"),
                difficulty = "Easy",
                category = "Action",
                traits = listOf("Lethal assassin", "Terrrible cook", "Sweet and polite", "Extreme physical strength", "Protects Anya fiercely"),
                abilities = listOf("Superhuman strength", "Needle blade combat", "Poison resistance", "Instant kick reflex"),
                famousQuote = "Would you permit me the honor of taking your life?",
                appearance = "Long black hair with golden headband, elegant black assassin dress, stiletto heels",
                spoilerSafeHints = listOf("Works a day job as a mild-mannered city hall clerk", "Accidentally destroys tennis balls with supersonic swings", "Entered a fake marriage to avoid government scrutiny")
            ),
            AnimeCharacter(
                id = "char_makima",
                name = "Makima",
                anime = "Chainsaw Man",
                aliases = listOf("Control Devil", "Special Division 4 Leader", "High-Ranking Devil Hunter"),
                difficulty = "Hard",
                category = "Villains",
                traits = listOf("Calm and polite demeanor", "Controls lesser beings", "Loves dogs", "Cinema marathon with Denji", "Secretly terrifying"),
                abilities = listOf("Control manipulation", "Finger gun blast (Bang)", "Remote psychic crushing", "Contract immortality"),
                famousQuote = "I like humans. In the same way that humans like dogs.",
                appearance = "Braided light red/orange hair, golden spiral ringed eyes, business suit and tie",
                spoilerSafeHints = listOf("Never blinks or breaks her polite composure in combat", "Wanted to erase bad concepts using a boy's heart devil", "Had a pack of well-groomed huskies in her apartment")
            ),
            AnimeCharacter(
                id = "char_spike",
                name = "Spike Spiegel",
                anime = "Cowboy Bebop",
                aliases = listOf("Space Cowboy", "Bebop Bounty Hunter", "Red Dragon Ex-Member"),
                difficulty = "Hard",
                category = "Legends",
                traits = listOf("Jeet Kune Do martial artist", "Cigarette in mouth", "Fake cybernetic right eye", "Haunted by past lover", "Swordfish II pilot"),
                abilities = listOf("Martial arts mastery", "Sharpshooter with Jericho 941", "Spacecraft dogfighting"),
                famousQuote = "Whatever happens, happens.",
                appearance = "Fluffy dark green hair, blue leisure suit, loosened yellow tie",
                spoilerSafeHints = listOf("Claims one eye sees the present, while the other looks at the past", "Traveled the cosmos eating bell peppers and beef with no beef", "Left his syndicate after faking his death")
            )
        )
    }
}

fun CharacterEntity.toDomain(): AnimeCharacter {
    return AnimeCharacter(
        id = id,
        name = name,
        anime = anime,
        aliases = if (aliasesJson.isBlank()) emptyList() else aliasesJson.split(";;;"),
        difficulty = difficulty,
        category = category,
        traits = if (traitsJson.isBlank()) emptyList() else traitsJson.split(";;;"),
        abilities = if (abilitiesJson.isBlank()) emptyList() else abilitiesJson.split(";;;"),
        famousQuote = famousQuote,
        appearance = appearance,
        spoilerSafeHints = if (hintsJson.isBlank()) emptyList() else hintsJson.split(";;;"),
        isCustom = isCustom
    )
}

fun AnimeCharacter.toEntity(): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        anime = anime,
        aliasesJson = aliases.joinToString(";;;"),
        difficulty = difficulty,
        category = category,
        traitsJson = traits.joinToString(";;;"),
        abilitiesJson = abilities.joinToString(";;;"),
        famousQuote = famousQuote,
        appearance = appearance,
        hintsJson = spoilerSafeHints.joinToString(";;;"),
        isCustom = isCustom
    )
}
