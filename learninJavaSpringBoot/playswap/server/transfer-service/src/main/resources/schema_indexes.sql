-- Database Optimization & Indexing Script for PlaySwap (PostgreSQL / MySQL)

-- 1. Indexing transfer_sessions for fast history lookup by user_id
CREATE INDEX IF NOT EXISTS idx_transfer_sessions_user_created ON transfer_sessions(user_id, created_at DESC);

-- 2. Indexing transfer_jobs for fast lookup by session_id
CREATE INDEX IF NOT EXISTS idx_transfer_jobs_session_id ON transfer_jobs(session_id);

-- 3. Indexing transfer_items for fast job items aggregation
CREATE INDEX IF NOT EXISTS idx_transfer_items_job_id_status ON transfer_items(job_id, status);

-- 4. Indexing transfer_history table for instantaneous user history retrieval
CREATE INDEX IF NOT EXISTS idx_transfer_history_user_created ON transfer_history(user_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_transfer_history_session_id ON transfer_history(session_id);

-- 5. Indexing song_mappings for zero-latency cache lookups during track matching
CREATE INDEX IF NOT EXISTS idx_song_mappings_lookup ON song_mappings(source_platform, source_track_id, destination_platform);
