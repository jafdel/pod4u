DO $$
BEGIN
REVOKE ALL ON SCHEMA musicbrainz FROM web_anon, authenticator;
DROP OWNED BY web_anon, authenticator;
COMMIT;
EXCEPTION
  WHEN invalid_role_specification THEN
  ROLLBACK;
  RAISE NOTICE 'caught invalid_role_specification';
END;
$$ LANGUAGE plpgsql;