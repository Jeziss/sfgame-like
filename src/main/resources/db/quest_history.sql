CREATE TABLE IF NOT EXISTS quest_history (
    id SERIAL PRIMARY KEY,
    player_id INTEGER NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    location VARCHAR(255) NOT NULL,
    energy_cost INTEGER NOT NULL,
    xp_reward INTEGER NOT NULL,
    gold_reward INTEGER NOT NULL,
    success BOOLEAN NOT NULL,
    completed_at TIMESTAMP NOT NULL
);
