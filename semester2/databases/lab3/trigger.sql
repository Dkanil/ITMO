CREATE OR REPLACE FUNCTION check_battery_charge()
    RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM battery
        WHERE (id = NEW.battery_id) AND (charge < 20)
    ) THEN
        RAISE NOTICE 'ВНИМАНИЕ: Заряд батареи меньше 20%%!';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_check_battery_charge
    BEFORE INSERT OR UPDATE ON antenna
    FOR EACH ROW
EXECUTE FUNCTION check_battery_charge();
