-- ============================================================
-- Job Master (jobs table)
-- ============================================================

-- ------------------------------------------------------------
-- Beginner jobs (40 types) - master_level: 10
-- ------------------------------------------------------------

INSERT INTO jobs (id, name, description, rank, master_level, base_hp, base_mp, base_attack, base_defense, base_speed, hp_per_level, mp_per_level, attack_per_level, defense_per_level, speed_per_level) VALUES
-- Warrior types
('bdd640fb-0667-4ad1-9c80-317fa3b1799d', 'Warrior', 'A basic frontline fighter with sword and shield', 'beginner', 10, 35, 5, 12, 10, 6, 3, 1, 2, 2, 1),
('23b8c1e9-3924-46de-beb1-3b9046685257', 'Swordsman', 'A combat specialist focused on swordsmanship', 'beginner', 10, 33, 8, 14, 8, 6, 3, 1, 3, 1, 1),
('bd9c66b3-ad3c-4d6d-9a3d-1fa7bc8960a9', 'Lancer', 'A soldier who attacks with long reach using a spear', 'beginner', 10, 32, 6, 13, 9, 6, 3, 1, 2, 2, 1),
('972a8469-1641-4f82-8b9d-2434e465e150', 'Axe Fighter', 'A powerhouse who crushes enemies with a heavy axe', 'beginner', 10, 34, 5, 15, 7, 6, 3, 1, 3, 1, 1),
('17fc695a-07a0-4a6e-8822-e8f36c031199', 'Martial Artist', 'A master of unarmed combat', 'beginner', 10, 30, 10, 13, 8, 6, 2, 1, 3, 1, 1),
('9a1de644-815e-46d1-bb8f-aa1837f8a88b', 'Knight Apprentice', 'A young soldier aspiring to become a knight', 'beginner', 10, 33, 7, 11, 12, 6, 3, 1, 2, 2, 1),
('b74d0fb1-32e7-4629-8fad-c1a606cb0fb3', 'Mercenary', 'A professional fighter for hire', 'beginner', 10, 32, 6, 14, 9, 6, 3, 1, 2, 2, 1),
('6b65a6a4-8b81-48f6-b38a-088ca65ed389', 'Gladiator', 'A warrior forged in the arena', 'beginner', 10, 31, 8, 13, 9, 6, 2, 1, 3, 2, 1),
-- Mage types
('47378190-96da-4dac-b2ff-5d2a386ecbe0', 'Mage', 'A spellcaster who wields offensive magic', 'beginner', 10, 20, 20, 5, 5, 5, 1, 3, 1, 1, 1),
('c241330b-01a9-471f-9e8a-774bcf36d58b', 'Sorcerer', 'A scholar who researches diverse magic', 'beginner', 10, 22, 18, 6, 6, 5, 1, 3, 1, 1, 1),
('6c307511-b2b9-437a-a8df-6ec4ce4a2bbd', 'Geomancer', 'A practitioner who manipulates wind and water', 'beginner', 10, 24, 17, 7, 7, 5, 1, 3, 1, 1, 1),
('371ecd7b-27cd-4130-8722-9389571aa876', 'Apprentice Summoner', 'A novice who calls forth creatures', 'beginner', 10, 23, 19, 5, 6, 5, 1, 3, 1, 1, 1),
('1a2a73ed-562b-4f79-8374-59eef50bea63', 'Alchemist', 'A magical scholar who transmutes materials', 'beginner', 10, 25, 16, 8, 7, 5, 2, 2, 1, 1, 1),
('5be6128e-18c2-4797-a142-ea7d17be3111', 'Hex Caster', 'A dark practitioner who wields curses', 'beginner', 10, 21, 19, 6, 5, 5, 1, 3, 1, 1, 1),
('43b7a3a6-9a8d-4a03-980d-7b71d8f56413', 'Spirit Caller', 'One who borrows power from contracted spirits', 'beginner', 10, 23, 18, 6, 6, 5, 1, 3, 1, 1, 1),
('759cde66-bacf-43d0-8b1f-9163ce9ff57f', 'Fortune Teller', 'One who reads the future through starlight', 'beginner', 10, 22, 17, 5, 6, 5, 1, 3, 1, 1, 1),
-- Cleric types
('ec1b8ca1-f91e-4d4c-9ff4-9b7889463e85', 'Cleric', 'A healer who mends allies with restorative magic', 'beginner', 10, 25, 18, 6, 8, 5, 2, 3, 1, 1, 1),
('4b0dbb41-8d52-48f1-942c-3fe860e7a113', 'Priest', 'One who wields holy power in service of the divine', 'beginner', 10, 26, 17, 7, 9, 5, 2, 2, 1, 2, 1),
('e2acf72f-9e57-4f7a-a0ee-89aed453dd32', 'Monk', 'A disciplined ascetic with a tempered body and mind', 'beginner', 10, 28, 15, 9, 10, 5, 2, 2, 1, 2, 1),
('3139d32c-93cd-49bf-9c94-1cf0dc98d2c1', 'Shrine Maiden', 'One who channels divine power through sacred dance', 'beginner', 10, 24, 19, 5, 7, 5, 1, 3, 1, 1, 1),
('a9488d99-0bbb-4599-91ce-5dd2b45ed1f0', 'Herbalist', 'A healer who uses knowledge of medicinal herbs', 'beginner', 10, 26, 16, 6, 7, 5, 2, 2, 1, 1, 1),
('fc377a4c-4a15-444d-85e7-ce8a3a578a8e', 'Chorister', 'One who heals allies through the power of song', 'beginner', 10, 23, 18, 5, 7, 5, 1, 3, 1, 1, 1),
('ddd1dfb2-3b98-4ef8-9af6-1a26146d3f31', 'Prayer Master', 'One who grants blessings through prayer', 'beginner', 10, 25, 17, 6, 8, 5, 2, 2, 1, 2, 1),
('7412b293-4729-4739-a14f-f3d719db3ad0', 'Exorcist Apprentice', 'A holy warrior who banishes evil', 'beginner', 10, 27, 15, 9, 9, 5, 2, 2, 2, 1, 1),
-- Rogue types
('29a3b2e9-5d65-4441-9588-42dea2bc372f', 'Thief', 'A swift trickster who outwits enemies', 'beginner', 10, 25, 10, 10, 6, 10, 2, 1, 2, 1, 2),
('ab9099a4-35a2-40ae-9af3-05535ec42e08', 'Archer', 'A marksman who strikes from a distance', 'beginner', 10, 26, 10, 11, 7, 10, 2, 1, 2, 1, 2),
('aefcfad8-efc8-4849-b3aa-7efe4458a885', 'Hunter', 'A tracker who pursues prey with wilderness expertise', 'beginner', 10, 28, 8, 12, 7, 10, 2, 1, 2, 2, 2),
('a28defe3-9bf0-4273-9247-6f57a5e5a5ab', 'Ninja Apprentice', 'A young shinobi learning the art of stealth', 'beginner', 10, 24, 12, 11, 6, 10, 2, 2, 2, 1, 2),
('3eabedcb-baa8-4dd4-88bd-64072bcfbe01', 'Pirate', 'A seafaring adventurer seeking treasure', 'beginner', 10, 30, 8, 12, 8, 10, 2, 1, 2, 2, 2),
('451b4cf3-6123-4df7-b656-af7229d4beef', 'Dancer', 'A performer who beguiles enemies through dance', 'beginner', 10, 23, 14, 8, 6, 10, 1, 2, 2, 1, 2),
('b02b61c4-a3d7-4628-ace6-6fa2fd5166e6', 'Scout', 'A specialist in reconnaissance and intelligence', 'beginner', 10, 25, 10, 10, 7, 10, 2, 1, 2, 1, 2),
('5304317f-af42-412f-b838-b3268e944239', 'Entertainer', 'A versatile traveling performer', 'beginner', 10, 26, 12, 9, 7, 10, 2, 2, 1, 1, 2),
-- Special types
('0e51f30d-c6a7-4e39-84b0-32ccd7c524a5', 'Merchant', 'One who profits through trade and commerce', 'beginner', 10, 27, 10, 8, 8, 7, 2, 1, 1, 2, 1),
('ce177b4e-0837-48a3-9261-a7ab3aa2e4f9', 'Blacksmith', 'A craftsman who forges weapons and armor', 'beginner', 10, 30, 7, 10, 10, 7, 2, 1, 2, 2, 1),
('10f1bc81-448a-4a9e-a6b2-bc5b50c187fc', 'Chef', 'One who revitalizes allies through cooking', 'beginner', 10, 28, 10, 8, 8, 7, 2, 1, 1, 1, 1),
('9132b63e-f162-47e4-a9c3-49e03602f8ac', 'Bard', 'One who inspires courage through song and poetry', 'beginner', 10, 24, 15, 6, 6, 7, 1, 2, 1, 1, 1),
('366eb16f-508e-4ad7-b7c9-3acfe059a0ee', 'Scholar', 'One who guides allies through knowledge and analysis', 'beginner', 10, 22, 16, 5, 6, 7, 1, 2, 1, 1, 1),
('e27a984d-6548-41d0-bfcd-9eb1a7cad415', 'Beast Tamer', 'One who bonds with animals', 'beginner', 10, 28, 12, 9, 7, 7, 2, 2, 2, 1, 1),
('24933b83-7577-40a9-a491-f0b2ea1fca65', 'Jester', 'A trickster who confounds enemies', 'beginner', 10, 24, 14, 8, 6, 7, 1, 2, 2, 1, 1),
('beb79919-3f22-4af8-a3be-d01d43cf2fde', 'Adventurer', 'A versatile traveler who can handle anything', 'beginner', 10, 28, 12, 10, 8, 7, 2, 2, 2, 1, 1)
ON CONFLICT DO NOTHING;

-- ------------------------------------------------------------
-- Intermediate jobs (30 types) - master_level: 20
-- ------------------------------------------------------------

INSERT INTO jobs (id, name, description, rank, master_level, base_hp, base_mp, base_attack, base_defense, base_speed, hp_per_level, mp_per_level, attack_per_level, defense_per_level, speed_per_level) VALUES
-- Warrior derivatives
('bf3c4c06-4343-48bc-89fa-6a688fb5d27b', 'Magic Swordsman', 'A dual-wielding master of sword and sorcery', 'intermediate', 20, 38, 25, 14, 10, 6, 4, 4, 4, 3, 1),
('956269f0-e5d7-4875-adad-d6c795a76d79', 'Paladin', 'A holy knight who protects allies with divine power', 'intermediate', 20, 42, 22, 12, 14, 6, 4, 3, 3, 4, 1),
('ff50bde4-3825-47b8-9cab-cc97663f1c97', 'Berserker', 'A frenzied warrior who fights with rage', 'intermediate', 20, 45, 15, 18, 8, 6, 5, 3, 5, 3, 1),
('7e570ddf-8270-40a8-a369-b584ff5e9ff0', 'Dragon Knight', 'A knight imbued with draconic power', 'intermediate', 20, 43, 20, 15, 13, 6, 4, 3, 4, 4, 1),
('dc713d96-0c0f-4195-817a-f08a1745d6d8', 'Battle Master', 'A fist fighter who has trained to the extreme', 'intermediate', 20, 40, 18, 16, 10, 7, 4, 3, 5, 3, 1),
('28f49481-a0a0-4dc4-a720-9bdf1c11f735', 'Guardian', 'A protector boasting impenetrable defense', 'intermediate', 20, 44, 16, 10, 16, 6, 5, 3, 3, 5, 1),
-- Mage derivatives
('98ae4334-6c12-4ce8-ae34-0454cac5b68c', 'Sage', 'A mage who has mastered both offense and healing', 'intermediate', 20, 35, 35, 8, 8, 5, 3, 5, 3, 3, 1),
('988c24c9-61b1-4d22-a280-1c4510435a10', 'Summoner', 'A caster who calls forth beings from other realms', 'intermediate', 20, 36, 33, 7, 7, 5, 3, 5, 3, 3, 1),
('405cacec-8774-49a9-b7d2-1e02ff01cf99', 'Spell Blade', 'A magic warrior who fights cloaked in mana', 'intermediate', 20, 40, 28, 12, 10, 6, 4, 4, 4, 3, 1),
('f143262f-dc5c-4eed-8da0-365bf89897b9', 'Necromancer', 'A dark mage who commands the dead', 'intermediate', 20, 35, 34, 9, 6, 5, 3, 5, 3, 3, 1),
('1d53434b-b881-49b9-ae27-0da702f06b90', 'Enchanter', 'A support mage specializing in augmentation', 'intermediate', 20, 36, 32, 7, 8, 5, 3, 5, 3, 3, 1),
('c0398710-8976-4334-a281-7efdae849217', 'Time Mage', 'A forbidden practitioner who manipulates time', 'intermediate', 20, 35, 34, 8, 7, 5, 3, 5, 3, 3, 1),
-- Cleric derivatives
('5715bd6f-a416-4293-84c2-e2e3444ea7c8', 'Bishop', 'A high-ranking cleric wielding powerful healing magic', 'intermediate', 20, 38, 30, 8, 10, 5, 3, 4, 3, 4, 1),
('287d06ca-6f4c-469a-8b22-d3081c8eaee9', 'Valkyrie', 'A battle maiden who soars across the battlefield', 'intermediate', 20, 40, 25, 13, 11, 6, 4, 4, 4, 3, 1),
('b8db0672-f42d-47cc-80d4-af5974273ca3', 'Exorcist', 'A specialist in banishing demons', 'intermediate', 20, 37, 28, 10, 9, 5, 3, 4, 3, 3, 1),
('f8cda88b-436d-46e2-b83c-fe0be037e5ed', 'Holy Knight', 'A knight who wields a blade empowered by faith', 'intermediate', 20, 41, 24, 12, 12, 6, 4, 4, 3, 4, 1),
('81f76d1c-2dbc-4134-830f-f46e8026695f', 'Druid', 'A wise one who commands the forces of nature', 'intermediate', 20, 38, 30, 9, 9, 5, 3, 5, 3, 3, 1),
('a013ac6e-deda-4e16-9b3d-bd5ce9a1fa6f', 'High Sage', 'A mentor versed in ancient knowledge', 'intermediate', 20, 36, 32, 8, 8, 5, 3, 5, 3, 3, 1),
-- Rogue derivatives
('81f631d4-a392-41a7-9777-a4774c66e0a8', 'Assassin', 'A shadow master who has perfected the killing arts', 'intermediate', 20, 37, 20, 16, 8, 10, 4, 3, 5, 3, 2),
('5fb8d16c-2720-497d-b2eb-d6899be578c7', 'Ninja', 'A master of stealth and ninjutsu', 'intermediate', 20, 38, 22, 14, 9, 10, 4, 3, 4, 3, 2),
('f4188f3f-8a14-4e62-a95b-4715c333e861', 'Ranger', 'A specialist in wilderness combat', 'intermediate', 20, 39, 20, 13, 10, 10, 4, 3, 4, 3, 2),
('eb2263dd-87c5-421e-ac24-a3c5c754108f', 'Sniper', 'A marksman capable of one-shot kills', 'intermediate', 20, 36, 18, 17, 7, 10, 3, 3, 5, 3, 2),
('7d154385-52fb-443b-9954-6eb400257ad1', 'Treasure Hunter', 'A master adventurer who seeks fortune', 'intermediate', 20, 38, 20, 12, 9, 10, 4, 3, 4, 3, 2),
('5cec4eb5-edd9-4831-9ca3-5cfb04fc6d82', 'Pirate King', 'A great pirate who rules the seas', 'intermediate', 20, 42, 18, 14, 11, 10, 4, 3, 4, 3, 2),
-- Special derivatives
('ce88cb2d-d4e8-4839-bc3e-058be0f3eab0', 'Rune Knight', 'A knight who wields the power of runes', 'intermediate', 20, 40, 26, 12, 11, 6, 4, 4, 4, 3, 1),
('3da9c2a9-0ed4-4f1a-bd4c-bf374eb93eff', 'Alchemy Warrior', 'One who applies alchemy in combat', 'intermediate', 20, 39, 24, 13, 10, 6, 4, 4, 4, 3, 1),
('14296c07-f26b-4776-913e-4de2e0c53cb8', 'Beast King', 'A fierce ruler who fights alongside beasts', 'intermediate', 20, 41, 20, 14, 10, 7, 4, 3, 4, 3, 1),
('d0e6e660-7c69-4ee1-bb5e-4bcf15ed6269', 'War Dancer', 'A performer who overwhelms foes with battle dance', 'intermediate', 20, 36, 24, 11, 8, 10, 3, 4, 4, 3, 2),
('885f6e66-c2b6-42c5-ba5d-310011b7e948', 'Master Thief', 'A phantom thief who has perfected the art of stealing', 'intermediate', 20, 37, 22, 13, 8, 10, 3, 3, 5, 3, 2),
('a8e56e0c-20de-435d-a031-d750c40db9b4', 'Strategist', 'A tactician who leads to victory through strategy', 'intermediate', 20, 36, 30, 9, 9, 6, 3, 5, 3, 3, 1)
ON CONFLICT DO NOTHING;

-- ------------------------------------------------------------
-- Advanced jobs (20 types) - master_level: 30
-- ------------------------------------------------------------

INSERT INTO jobs (id, name, description, rank, master_level, base_hp, base_mp, base_attack, base_defense, base_speed, hp_per_level, mp_per_level, attack_per_level, defense_per_level, speed_per_level) VALUES
('2a45c2ab-8cbf-4db0-b264-accc79ac1b1e', 'Demon Lord', 'A king who has mastered the power of darkness', 'advanced', 30, 50, 45, 18, 14, 6, 5, 6, 5, 4, 1),
('9b49bd26-df57-459a-8715-a10343dac043', 'Holy King', 'A king cloaked in sacred light', 'advanced', 30, 52, 40, 16, 16, 6, 5, 5, 5, 5, 1),
('edcd465e-3638-4821-b6e0-7cc06c52c49f', 'Sword Saint', 'A master who has reached the pinnacle of swordsmanship', 'advanced', 30, 48, 30, 22, 14, 5, 5, 4, 6, 4, 1),
('b09b2a5c-badc-432a-8159-0f538a0f4efb', 'Archmage', 'One who wields the highest tier of magic', 'advanced', 30, 42, 45, 12, 10, 5, 4, 6, 4, 4, 1),
('66245bfa-4fcc-439a-b683-d2e6337ea2df', 'Grand Cleric', 'The highest authority of holy power', 'advanced', 30, 45, 42, 10, 14, 5, 5, 6, 4, 5, 1),
('5f987c71-a65e-488e-abf3-ad39fec21bbe', 'Dark Knight', 'The mightiest knight wielding the power of darkness', 'advanced', 30, 50, 32, 20, 15, 6, 5, 5, 6, 4, 1),
('7394988f-847f-49b4-a64d-1bcb702753a1', 'Dragoon', 'A knight who has forged a pact with dragons', 'advanced', 30, 52, 30, 19, 16, 6, 6, 4, 5, 5, 1),
('1064005c-3985-43cf-bf76-be1d1efa2197', 'Shadow King', 'The apex of assassins who commands shadows', 'advanced', 30, 44, 35, 20, 11, 10, 5, 5, 6, 4, 2),
('8dcdcd03-969b-4662-8562-8059568cc69b', 'Summon King', 'One who commands the mightiest summons', 'advanced', 30, 43, 44, 13, 11, 6, 4, 6, 4, 4, 1),
('01d74256-3860-4ab6-96a4-02f23ae8cc93', 'War God', 'One who has attained the pinnacle of martial arts', 'advanced', 30, 50, 28, 22, 15, 6, 6, 4, 6, 5, 1),
('0f1259e0-a18f-46b6-b535-106e122c9a56', 'Grand Sage', 'One who presides over all knowledge', 'advanced', 30, 44, 44, 11, 11, 5, 4, 6, 4, 4, 1),
('080aadfb-e7c9-4b26-9141-25c63a9bedd4', 'Shield King', 'An iron-walled king of absolute defense', 'advanced', 30, 55, 28, 14, 20, 6, 6, 4, 4, 6, 1),
('839fbc50-1223-4513-9496-f63cdc1110c1', 'Sky Archer', 'A master archer who pierces the heavens', 'advanced', 30, 45, 30, 19, 12, 10, 5, 4, 6, 4, 2),
('7c441fe7-ab42-40a7-874a-493b3ceddf2d', 'Monster Lord', 'A legendary tamer who commands magical beasts', 'advanced', 30, 48, 34, 16, 13, 6, 5, 5, 5, 4, 1),
('b92da22b-21df-406f-8a0b-3c3336d8393a', 'Chrono Mage', 'A forbidden user who manipulates spacetime', 'advanced', 30, 42, 44, 13, 10, 5, 4, 6, 5, 4, 1),
('93829b43-922f-415a-a1e3-db63ef7ddc76', 'Hero', 'A true hero spoken of in legends', 'advanced', 30, 50, 35, 18, 15, 6, 5, 5, 5, 5, 1),
('7914c120-c8dc-419f-be35-11287900f7f9', 'Conqueror', 'A ruler who dominates all through might', 'advanced', 30, 52, 28, 21, 16, 6, 6, 4, 6, 5, 1),
('1825bc54-30be-445f-a835-14f2ceb81f9d', 'Saint', 'One who performs sacred miracles', 'advanced', 30, 44, 42, 10, 13, 5, 4, 6, 4, 5, 1),
('5ab33edf-6e59-4ed3-a8b3-17fa18d0752b', 'Reaper', 'A terrifying existence that harvests souls', 'advanced', 30, 46, 36, 19, 10, 6, 5, 5, 6, 4, 1),
('dd2467ac-778e-4db3-a93d-ffbc6c6fa611', 'Omni Master', 'One who commands every technique', 'advanced', 30, 48, 38, 15, 13, 7, 5, 5, 5, 5, 1)
ON CONFLICT DO NOTHING;

-- ------------------------------------------------------------
-- Master jobs (10 types) - master_level: 50
-- ------------------------------------------------------------

INSERT INTO jobs (id, name, description, rank, master_level, base_hp, base_mp, base_attack, base_defense, base_speed, hp_per_level, mp_per_level, attack_per_level, defense_per_level, speed_per_level) VALUES
('a748dbcf-ac61-4e63-8dde-29a6baa4b71a', 'Divine Dragon King', 'The mightiest being possessing both dragon and divine power', 'master', 50, 65, 55, 25, 22, 6, 8, 8, 8, 7, 1),
('0f844fef-1931-49ee-a56c-0941fbf24050', 'Celestial Emperor', 'The supreme sovereign who rules the heavens', 'master', 50, 60, 60, 22, 20, 6, 7, 9, 7, 7, 1),
('ccf3a171-56dc-4907-ba6c-34ab6712303a', 'Demon God', 'A deity who has reached the pinnacle of magical power', 'master', 50, 55, 60, 24, 18, 6, 7, 9, 8, 6, 1),
('310c0c00-3fa7-4104-9bf9-0e27dc96925e', 'Sword God', 'One who has attained the ultimate mastery of the blade', 'master', 50, 60, 45, 28, 20, 6, 8, 7, 9, 7, 1),
('23e2fcb4-72d8-467d-894a-05e430b187ef', 'Holy Emperor', 'The ultimate embodiment of holy power', 'master', 50, 58, 55, 20, 22, 6, 7, 8, 7, 8, 1),
('766ecb15-474e-4c19-aef9-12766c006f61', 'Nether King', 'An absolute ruler who governs the underworld', 'master', 50, 56, 58, 24, 19, 6, 7, 9, 8, 7, 1),
('134c6c92-ec5b-427c-9fde-4fbf3ff350bf', 'Creator God', 'One who possesses the power of genesis to create all things', 'master', 50, 55, 60, 20, 20, 6, 7, 9, 7, 7, 1),
('db20a56e-dc81-4fe7-8eda-8bbb71710434', 'Destroyer God', 'One who possesses the ultimate power to annihilate all', 'master', 50, 62, 50, 28, 18, 6, 8, 8, 9, 6, 1),
('a6f2f7b8-0cf3-4b58-9910-8be58ce21ea3', 'Fate God', 'A transcendent being who manipulates destiny', 'master', 50, 55, 58, 22, 20, 6, 7, 9, 8, 7, 1),
('03c72ba8-d605-4770-8a63-f881ffd0f9d5', 'Transcendent', 'The ultimate existence who has mastered every profession', 'master', 50, 62, 58, 25, 22, 6, 8, 9, 8, 8, 1)
ON CONFLICT DO NOTHING;

-- ============================================================
-- Job Requirements (job_requirements table)
-- ============================================================

-- ------------------------------------------------------------
-- Beginner -> Intermediate (2-job combinations)
-- ------------------------------------------------------------

INSERT INTO job_requirements (job_id, required_job_id) VALUES
-- Magic Swordsman <- Warrior + Mage
('bf3c4c06-4343-48bc-89fa-6a688fb5d27b', 'bdd640fb-0667-4ad1-9c80-317fa3b1799d'),
('bf3c4c06-4343-48bc-89fa-6a688fb5d27b', '47378190-96da-4dac-b2ff-5d2a386ecbe0'),
-- Paladin <- Warrior + Cleric
('956269f0-e5d7-4875-adad-d6c795a76d79', 'bdd640fb-0667-4ad1-9c80-317fa3b1799d'),
('956269f0-e5d7-4875-adad-d6c795a76d79', 'ec1b8ca1-f91e-4d4c-9ff4-9b7889463e85'),
-- Berserker <- Warrior + Martial Artist
('ff50bde4-3825-47b8-9cab-cc97663f1c97', 'bdd640fb-0667-4ad1-9c80-317fa3b1799d'),
('ff50bde4-3825-47b8-9cab-cc97663f1c97', '17fc695a-07a0-4a6e-8822-e8f36c031199'),
-- Dragon Knight <- Knight Apprentice + Lancer
('7e570ddf-8270-40a8-a369-b584ff5e9ff0', '9a1de644-815e-46d1-bb8f-aa1837f8a88b'),
('7e570ddf-8270-40a8-a369-b584ff5e9ff0', 'bd9c66b3-ad3c-4d6d-9a3d-1fa7bc8960a9'),
-- Battle Master <- Martial Artist + Gladiator
('dc713d96-0c0f-4195-817a-f08a1745d6d8', '17fc695a-07a0-4a6e-8822-e8f36c031199'),
('dc713d96-0c0f-4195-817a-f08a1745d6d8', '6b65a6a4-8b81-48f6-b38a-088ca65ed389'),
-- Guardian <- Knight Apprentice + Monk
('28f49481-a0a0-4dc4-a720-9bdf1c11f735', '9a1de644-815e-46d1-bb8f-aa1837f8a88b'),
('28f49481-a0a0-4dc4-a720-9bdf1c11f735', 'e2acf72f-9e57-4f7a-a0ee-89aed453dd32'),
-- Sage <- Mage + Cleric
('98ae4334-6c12-4ce8-ae34-0454cac5b68c', '47378190-96da-4dac-b2ff-5d2a386ecbe0'),
('98ae4334-6c12-4ce8-ae34-0454cac5b68c', 'ec1b8ca1-f91e-4d4c-9ff4-9b7889463e85'),
-- Summoner <- Apprentice Summoner + Spirit Caller
('988c24c9-61b1-4d22-a280-1c4510435a10', '371ecd7b-27cd-4130-8722-9389571aa876'),
('988c24c9-61b1-4d22-a280-1c4510435a10', '43b7a3a6-9a8d-4a03-980d-7b71d8f56413'),
-- Spell Blade <- Swordsman + Sorcerer
('405cacec-8774-49a9-b7d2-1e02ff01cf99', '23b8c1e9-3924-46de-beb1-3b9046685257'),
('405cacec-8774-49a9-b7d2-1e02ff01cf99', 'c241330b-01a9-471f-9e8a-774bcf36d58b'),
-- Necromancer <- Hex Caster + Alchemist
('f143262f-dc5c-4eed-8da0-365bf89897b9', '5be6128e-18c2-4797-a142-ea7d17be3111'),
('f143262f-dc5c-4eed-8da0-365bf89897b9', '1a2a73ed-562b-4f79-8374-59eef50bea63'),
-- Enchanter <- Sorcerer + Fortune Teller
('1d53434b-b881-49b9-ae27-0da702f06b90', 'c241330b-01a9-471f-9e8a-774bcf36d58b'),
('1d53434b-b881-49b9-ae27-0da702f06b90', '759cde66-bacf-43d0-8b1f-9163ce9ff57f'),
-- Time Mage <- Geomancer + Fortune Teller
('c0398710-8976-4334-a281-7efdae849217', '6c307511-b2b9-437a-a8df-6ec4ce4a2bbd'),
('c0398710-8976-4334-a281-7efdae849217', '759cde66-bacf-43d0-8b1f-9163ce9ff57f'),
-- Bishop <- Cleric + Priest
('5715bd6f-a416-4293-84c2-e2e3444ea7c8', 'ec1b8ca1-f91e-4d4c-9ff4-9b7889463e85'),
('5715bd6f-a416-4293-84c2-e2e3444ea7c8', '4b0dbb41-8d52-48f1-942c-3fe860e7a113'),
-- Valkyrie <- Warrior + Shrine Maiden
('287d06ca-6f4c-469a-8b22-d3081c8eaee9', 'bdd640fb-0667-4ad1-9c80-317fa3b1799d'),
('287d06ca-6f4c-469a-8b22-d3081c8eaee9', '3139d32c-93cd-49bf-9c94-1cf0dc98d2c1'),
-- Exorcist <- Exorcist Apprentice + Priest
('b8db0672-f42d-47cc-80d4-af5974273ca3', '7412b293-4729-4739-a14f-f3d719db3ad0'),
('b8db0672-f42d-47cc-80d4-af5974273ca3', '4b0dbb41-8d52-48f1-942c-3fe860e7a113'),
-- Holy Knight <- Knight Apprentice + Cleric
('f8cda88b-436d-46e2-b83c-fe0be037e5ed', '9a1de644-815e-46d1-bb8f-aa1837f8a88b'),
('f8cda88b-436d-46e2-b83c-fe0be037e5ed', 'ec1b8ca1-f91e-4d4c-9ff4-9b7889463e85'),
-- Druid <- Herbalist + Spirit Caller
('81f76d1c-2dbc-4134-830f-f46e8026695f', 'a9488d99-0bbb-4599-91ce-5dd2b45ed1f0'),
('81f76d1c-2dbc-4134-830f-f46e8026695f', '43b7a3a6-9a8d-4a03-980d-7b71d8f56413'),
-- High Sage <- Scholar + Prayer Master
('a013ac6e-deda-4e16-9b3d-bd5ce9a1fa6f', '366eb16f-508e-4ad7-b7c9-3acfe059a0ee'),
('a013ac6e-deda-4e16-9b3d-bd5ce9a1fa6f', 'ddd1dfb2-3b98-4ef8-9af6-1a26146d3f31'),
-- Assassin <- Thief + Archer
('81f631d4-a392-41a7-9777-a4774c66e0a8', '29a3b2e9-5d65-4441-9588-42dea2bc372f'),
('81f631d4-a392-41a7-9777-a4774c66e0a8', 'ab9099a4-35a2-40ae-9af3-05535ec42e08'),
-- Ninja <- Ninja Apprentice + Thief
('5fb8d16c-2720-497d-b2eb-d6899be578c7', 'a28defe3-9bf0-4273-9247-6f57a5e5a5ab'),
('5fb8d16c-2720-497d-b2eb-d6899be578c7', '29a3b2e9-5d65-4441-9588-42dea2bc372f'),
-- Ranger <- Hunter + Archer
('f4188f3f-8a14-4e62-a95b-4715c333e861', 'aefcfad8-efc8-4849-b3aa-7efe4458a885'),
('f4188f3f-8a14-4e62-a95b-4715c333e861', 'ab9099a4-35a2-40ae-9af3-05535ec42e08'),
-- Sniper <- Archer + Scout
('eb2263dd-87c5-421e-ac24-a3c5c754108f', 'ab9099a4-35a2-40ae-9af3-05535ec42e08'),
('eb2263dd-87c5-421e-ac24-a3c5c754108f', 'b02b61c4-a3d7-4628-ace6-6fa2fd5166e6'),
-- Treasure Hunter <- Thief + Adventurer
('7d154385-52fb-443b-9954-6eb400257ad1', '29a3b2e9-5d65-4441-9588-42dea2bc372f'),
('7d154385-52fb-443b-9954-6eb400257ad1', 'beb79919-3f22-4af8-a3be-d01d43cf2fde'),
-- Pirate King <- Pirate + Mercenary
('5cec4eb5-edd9-4831-9ca3-5cfb04fc6d82', '3eabedcb-baa8-4dd4-88bd-64072bcfbe01'),
('5cec4eb5-edd9-4831-9ca3-5cfb04fc6d82', 'b74d0fb1-32e7-4629-8fad-c1a606cb0fb3'),
-- Rune Knight <- Swordsman + Geomancer
('ce88cb2d-d4e8-4839-bc3e-058be0f3eab0', '23b8c1e9-3924-46de-beb1-3b9046685257'),
('ce88cb2d-d4e8-4839-bc3e-058be0f3eab0', '6c307511-b2b9-437a-a8df-6ec4ce4a2bbd'),
-- Alchemy Warrior <- Alchemist + Axe Fighter
('3da9c2a9-0ed4-4f1a-bd4c-bf374eb93eff', '1a2a73ed-562b-4f79-8374-59eef50bea63'),
('3da9c2a9-0ed4-4f1a-bd4c-bf374eb93eff', '972a8469-1641-4f82-8b9d-2434e465e150'),
-- Beast King <- Beast Tamer + Hunter
('14296c07-f26b-4776-913e-4de2e0c53cb8', 'e27a984d-6548-41d0-bfcd-9eb1a7cad415'),
('14296c07-f26b-4776-913e-4de2e0c53cb8', 'aefcfad8-efc8-4849-b3aa-7efe4458a885'),
-- War Dancer <- Dancer + Bard
('d0e6e660-7c69-4ee1-bb5e-4bcf15ed6269', '451b4cf3-6123-4df7-b656-af7229d4beef'),
('d0e6e660-7c69-4ee1-bb5e-4bcf15ed6269', '9132b63e-f162-47e4-a9c3-49e03602f8ac'),
-- Master Thief <- Thief + Jester
('885f6e66-c2b6-42c5-ba5d-310011b7e948', '29a3b2e9-5d65-4441-9588-42dea2bc372f'),
('885f6e66-c2b6-42c5-ba5d-310011b7e948', '24933b83-7577-40a9-a491-f0b2ea1fca65'),
-- Strategist <- Scholar + Merchant
('a8e56e0c-20de-435d-a031-d750c40db9b4', '366eb16f-508e-4ad7-b7c9-3acfe059a0ee'),
('a8e56e0c-20de-435d-a031-d750c40db9b4', '0e51f30d-c6a7-4e39-84b0-32ccd7c524a5')
ON CONFLICT DO NOTHING;

-- ------------------------------------------------------------
-- Intermediate -> Advanced (2-3 job combinations)
-- ------------------------------------------------------------

INSERT INTO job_requirements (job_id, required_job_id) VALUES
-- Demon Lord <- Sage + Necromancer + Time Mage
('2a45c2ab-8cbf-4db0-b264-accc79ac1b1e', '98ae4334-6c12-4ce8-ae34-0454cac5b68c'),
('2a45c2ab-8cbf-4db0-b264-accc79ac1b1e', 'f143262f-dc5c-4eed-8da0-365bf89897b9'),
('2a45c2ab-8cbf-4db0-b264-accc79ac1b1e', 'c0398710-8976-4334-a281-7efdae849217'),
-- Holy King <- Paladin + Bishop + Holy Knight
('9b49bd26-df57-459a-8715-a10343dac043', '956269f0-e5d7-4875-adad-d6c795a76d79'),
('9b49bd26-df57-459a-8715-a10343dac043', '5715bd6f-a416-4293-84c2-e2e3444ea7c8'),
('9b49bd26-df57-459a-8715-a10343dac043', 'f8cda88b-436d-46e2-b83c-fe0be037e5ed'),
-- Sword Saint <- Magic Swordsman + Battle Master
('edcd465e-3638-4821-b6e0-7cc06c52c49f', 'bf3c4c06-4343-48bc-89fa-6a688fb5d27b'),
('edcd465e-3638-4821-b6e0-7cc06c52c49f', 'dc713d96-0c0f-4195-817a-f08a1745d6d8'),
-- Archmage <- Sage + Enchanter + Summoner
('b09b2a5c-badc-432a-8159-0f538a0f4efb', '98ae4334-6c12-4ce8-ae34-0454cac5b68c'),
('b09b2a5c-badc-432a-8159-0f538a0f4efb', '1d53434b-b881-49b9-ae27-0da702f06b90'),
('b09b2a5c-badc-432a-8159-0f538a0f4efb', '988c24c9-61b1-4d22-a280-1c4510435a10'),
-- Grand Cleric <- Bishop + Druid + High Sage
('66245bfa-4fcc-439a-b683-d2e6337ea2df', '5715bd6f-a416-4293-84c2-e2e3444ea7c8'),
('66245bfa-4fcc-439a-b683-d2e6337ea2df', '81f76d1c-2dbc-4134-830f-f46e8026695f'),
('66245bfa-4fcc-439a-b683-d2e6337ea2df', 'a013ac6e-deda-4e16-9b3d-bd5ce9a1fa6f'),
-- Dark Knight <- Berserker + Necromancer
('5f987c71-a65e-488e-abf3-ad39fec21bbe', 'ff50bde4-3825-47b8-9cab-cc97663f1c97'),
('5f987c71-a65e-488e-abf3-ad39fec21bbe', 'f143262f-dc5c-4eed-8da0-365bf89897b9'),
-- Dragoon <- Dragon Knight + Guardian
('7394988f-847f-49b4-a64d-1bcb702753a1', '7e570ddf-8270-40a8-a369-b584ff5e9ff0'),
('7394988f-847f-49b4-a64d-1bcb702753a1', '28f49481-a0a0-4dc4-a720-9bdf1c11f735'),
-- Shadow King <- Assassin + Ninja + Master Thief
('1064005c-3985-43cf-bf76-be1d1efa2197', '81f631d4-a392-41a7-9777-a4774c66e0a8'),
('1064005c-3985-43cf-bf76-be1d1efa2197', '5fb8d16c-2720-497d-b2eb-d6899be578c7'),
('1064005c-3985-43cf-bf76-be1d1efa2197', '885f6e66-c2b6-42c5-ba5d-310011b7e948'),
-- Summon King <- Summoner + Enchanter
('8dcdcd03-969b-4662-8562-8059568cc69b', '988c24c9-61b1-4d22-a280-1c4510435a10'),
('8dcdcd03-969b-4662-8562-8059568cc69b', '1d53434b-b881-49b9-ae27-0da702f06b90'),
-- War God <- Battle Master + Berserker + Guardian
('01d74256-3860-4ab6-96a4-02f23ae8cc93', 'dc713d96-0c0f-4195-817a-f08a1745d6d8'),
('01d74256-3860-4ab6-96a4-02f23ae8cc93', 'ff50bde4-3825-47b8-9cab-cc97663f1c97'),
('01d74256-3860-4ab6-96a4-02f23ae8cc93', '28f49481-a0a0-4dc4-a720-9bdf1c11f735'),
-- Grand Sage <- Sage + High Sage + Strategist
('0f1259e0-a18f-46b6-b535-106e122c9a56', '98ae4334-6c12-4ce8-ae34-0454cac5b68c'),
('0f1259e0-a18f-46b6-b535-106e122c9a56', 'a013ac6e-deda-4e16-9b3d-bd5ce9a1fa6f'),
('0f1259e0-a18f-46b6-b535-106e122c9a56', 'a8e56e0c-20de-435d-a031-d750c40db9b4'),
-- Shield King <- Guardian + Paladin + Holy Knight
('080aadfb-e7c9-4b26-9141-25c63a9bedd4', '28f49481-a0a0-4dc4-a720-9bdf1c11f735'),
('080aadfb-e7c9-4b26-9141-25c63a9bedd4', '956269f0-e5d7-4875-adad-d6c795a76d79'),
('080aadfb-e7c9-4b26-9141-25c63a9bedd4', 'f8cda88b-436d-46e2-b83c-fe0be037e5ed'),
-- Sky Archer <- Sniper + Ranger
('839fbc50-1223-4513-9496-f63cdc1110c1', 'eb2263dd-87c5-421e-ac24-a3c5c754108f'),
('839fbc50-1223-4513-9496-f63cdc1110c1', 'f4188f3f-8a14-4e62-a95b-4715c333e861'),
-- Monster Lord <- Beast King + Summoner
('7c441fe7-ab42-40a7-874a-493b3ceddf2d', '14296c07-f26b-4776-913e-4de2e0c53cb8'),
('7c441fe7-ab42-40a7-874a-493b3ceddf2d', '988c24c9-61b1-4d22-a280-1c4510435a10'),
-- Chrono Mage <- Time Mage + Sage + Enchanter
('b92da22b-21df-406f-8a0b-3c3336d8393a', 'c0398710-8976-4334-a281-7efdae849217'),
('b92da22b-21df-406f-8a0b-3c3336d8393a', '98ae4334-6c12-4ce8-ae34-0454cac5b68c'),
('b92da22b-21df-406f-8a0b-3c3336d8393a', '1d53434b-b881-49b9-ae27-0da702f06b90'),
-- Hero <- Magic Swordsman + Paladin + Ranger
('93829b43-922f-415a-a1e3-db63ef7ddc76', 'bf3c4c06-4343-48bc-89fa-6a688fb5d27b'),
('93829b43-922f-415a-a1e3-db63ef7ddc76', '956269f0-e5d7-4875-adad-d6c795a76d79'),
('93829b43-922f-415a-a1e3-db63ef7ddc76', 'f4188f3f-8a14-4e62-a95b-4715c333e861'),
-- Conqueror <- Berserker + Dragon Knight + Pirate King
('7914c120-c8dc-419f-be35-11287900f7f9', 'ff50bde4-3825-47b8-9cab-cc97663f1c97'),
('7914c120-c8dc-419f-be35-11287900f7f9', '7e570ddf-8270-40a8-a369-b584ff5e9ff0'),
('7914c120-c8dc-419f-be35-11287900f7f9', '5cec4eb5-edd9-4831-9ca3-5cfb04fc6d82'),
-- Saint <- Bishop + Valkyrie + Druid
('1825bc54-30be-445f-a835-14f2ceb81f9d', '5715bd6f-a416-4293-84c2-e2e3444ea7c8'),
('1825bc54-30be-445f-a835-14f2ceb81f9d', '287d06ca-6f4c-469a-8b22-d3081c8eaee9'),
('1825bc54-30be-445f-a835-14f2ceb81f9d', '81f76d1c-2dbc-4134-830f-f46e8026695f'),
-- Reaper <- Assassin + Necromancer + Exorcist
('5ab33edf-6e59-4ed3-a8b3-17fa18d0752b', '81f631d4-a392-41a7-9777-a4774c66e0a8'),
('5ab33edf-6e59-4ed3-a8b3-17fa18d0752b', 'f143262f-dc5c-4eed-8da0-365bf89897b9'),
('5ab33edf-6e59-4ed3-a8b3-17fa18d0752b', 'b8db0672-f42d-47cc-80d4-af5974273ca3'),
-- Omni Master <- Spell Blade + Rune Knight + Treasure Hunter
('dd2467ac-778e-4db3-a93d-ffbc6c6fa611', '405cacec-8774-49a9-b7d2-1e02ff01cf99'),
('dd2467ac-778e-4db3-a93d-ffbc6c6fa611', 'ce88cb2d-d4e8-4839-bc3e-058be0f3eab0'),
('dd2467ac-778e-4db3-a93d-ffbc6c6fa611', '7d154385-52fb-443b-9954-6eb400257ad1')
ON CONFLICT DO NOTHING;

-- ------------------------------------------------------------
-- Advanced -> Master (2-3 job combinations)
-- ------------------------------------------------------------

INSERT INTO job_requirements (job_id, required_job_id) VALUES
-- Divine Dragon King <- Dragoon + Holy King + Demon Lord
('a748dbcf-ac61-4e63-8dde-29a6baa4b71a', '7394988f-847f-49b4-a64d-1bcb702753a1'),
('a748dbcf-ac61-4e63-8dde-29a6baa4b71a', '9b49bd26-df57-459a-8715-a10343dac043'),
('a748dbcf-ac61-4e63-8dde-29a6baa4b71a', '2a45c2ab-8cbf-4db0-b264-accc79ac1b1e'),
-- Celestial Emperor <- Holy King + Grand Sage + Shield King
('0f844fef-1931-49ee-a56c-0941fbf24050', '9b49bd26-df57-459a-8715-a10343dac043'),
('0f844fef-1931-49ee-a56c-0941fbf24050', '0f1259e0-a18f-46b6-b535-106e122c9a56'),
('0f844fef-1931-49ee-a56c-0941fbf24050', '080aadfb-e7c9-4b26-9141-25c63a9bedd4'),
-- Demon God <- Demon Lord + Archmage + Chrono Mage
('ccf3a171-56dc-4907-ba6c-34ab6712303a', '2a45c2ab-8cbf-4db0-b264-accc79ac1b1e'),
('ccf3a171-56dc-4907-ba6c-34ab6712303a', 'b09b2a5c-badc-432a-8159-0f538a0f4efb'),
('ccf3a171-56dc-4907-ba6c-34ab6712303a', 'b92da22b-21df-406f-8a0b-3c3336d8393a'),
-- Sword God <- Sword Saint + War God + Conqueror
('310c0c00-3fa7-4104-9bf9-0e27dc96925e', 'edcd465e-3638-4821-b6e0-7cc06c52c49f'),
('310c0c00-3fa7-4104-9bf9-0e27dc96925e', '01d74256-3860-4ab6-96a4-02f23ae8cc93'),
('310c0c00-3fa7-4104-9bf9-0e27dc96925e', '7914c120-c8dc-419f-be35-11287900f7f9'),
-- Holy Emperor <- Holy King + Grand Cleric + Saint
('23e2fcb4-72d8-467d-894a-05e430b187ef', '9b49bd26-df57-459a-8715-a10343dac043'),
('23e2fcb4-72d8-467d-894a-05e430b187ef', '66245bfa-4fcc-439a-b683-d2e6337ea2df'),
('23e2fcb4-72d8-467d-894a-05e430b187ef', '1825bc54-30be-445f-a835-14f2ceb81f9d'),
-- Nether King <- Demon Lord + Dark Knight + Reaper
('766ecb15-474e-4c19-aef9-12766c006f61', '2a45c2ab-8cbf-4db0-b264-accc79ac1b1e'),
('766ecb15-474e-4c19-aef9-12766c006f61', '5f987c71-a65e-488e-abf3-ad39fec21bbe'),
('766ecb15-474e-4c19-aef9-12766c006f61', '5ab33edf-6e59-4ed3-a8b3-17fa18d0752b'),
-- Creator God <- Archmage + Summon King + Grand Sage
('134c6c92-ec5b-427c-9fde-4fbf3ff350bf', 'b09b2a5c-badc-432a-8159-0f538a0f4efb'),
('134c6c92-ec5b-427c-9fde-4fbf3ff350bf', '8dcdcd03-969b-4662-8562-8059568cc69b'),
('134c6c92-ec5b-427c-9fde-4fbf3ff350bf', '0f1259e0-a18f-46b6-b535-106e122c9a56'),
-- Destroyer God <- Conqueror + War God + Dark Knight
('db20a56e-dc81-4fe7-8eda-8bbb71710434', '7914c120-c8dc-419f-be35-11287900f7f9'),
('db20a56e-dc81-4fe7-8eda-8bbb71710434', '01d74256-3860-4ab6-96a4-02f23ae8cc93'),
('db20a56e-dc81-4fe7-8eda-8bbb71710434', '5f987c71-a65e-488e-abf3-ad39fec21bbe'),
-- Fate God <- Chrono Mage + Grand Sage + Omni Master
('a6f2f7b8-0cf3-4b58-9910-8be58ce21ea3', 'b92da22b-21df-406f-8a0b-3c3336d8393a'),
('a6f2f7b8-0cf3-4b58-9910-8be58ce21ea3', '0f1259e0-a18f-46b6-b535-106e122c9a56'),
('a6f2f7b8-0cf3-4b58-9910-8be58ce21ea3', 'dd2467ac-778e-4db3-a93d-ffbc6c6fa611'),
-- Transcendent <- Hero + Omni Master + Grand Sage
('03c72ba8-d605-4770-8a63-f881ffd0f9d5', '93829b43-922f-415a-a1e3-db63ef7ddc76'),
('03c72ba8-d605-4770-8a63-f881ffd0f9d5', 'dd2467ac-778e-4db3-a93d-ffbc6c6fa611'),
('03c72ba8-d605-4770-8a63-f881ffd0f9d5', '0f1259e0-a18f-46b6-b535-106e122c9a56')
ON CONFLICT DO NOTHING;
