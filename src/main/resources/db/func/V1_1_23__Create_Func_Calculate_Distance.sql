-- Support for calculate distance
CREATE OR REPLACE FUNCTION public."func_deg2rad"(
	deg double precision)
    RETURNS double precision
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE

AS $BODY$
DECLARE
tmp double precision;
BEGIN
	tmp := (SELECT pi());
RETURN deg * (tmp/180);
END;$BODY$;

ALTER FUNCTION public."func_deg2rad"(double precision)
    OWNER TO postgres;

-- Calculate distance in km between two points
CREATE OR REPLACE FUNCTION func_get_distance_from_lat_lng_in_km(lat1 double precision, lng1 double precision, lat2 double precision, lng2 double precision)
    RETURNS double precision
    LANGUAGE 'plpgsql'
AS
$$
DECLARE
R double precision;
    dLat double precision;
    dLng double precision;
    tmp1 double precision;
    tmp2 double precision;
    res double precision;
BEGIN
    R := 6371; -- Radius of the earth in km
    dLat := (SELECT func_deg2rad(lat2 - lat1));
    dLng := (SELECT func_deg2rad(lng2 - lng1));
    tmp1 := sin(dLat/2) * sin(dLat/2) + cos((SELECT func_deg2rad(lat1))) * cos((SELECT func_deg2rad(lat2))) * sin(dLng/2) * sin(dLng/2);
    tmp2 = 2 * atan2(sqrt(tmp1), sqrt(1-tmp1));
return R * tmp2; -- Distance in km
END;
$$;

-- Execute get list of locations
-- SELECT * FROM func_get_nearest_locations(10.768519252055944, 106.64416943804024, 'STORE', 3, 1);
CREATE OR REPLACE FUNCTION public.func_get_nearest_locations(
	lat double precision,
	lng double precision,
	targettype character varying,
	dlimit integer,
	doffset integer DEFAULT 0,
	keyword character varying DEFAULT '')
    RETURNS TABLE(id bigint, created_at timestamp without time zone, created_by bigint, updated_at timestamp without time zone, updated_by bigint, address character varying, city character varying, country character varying, state character varying, zipcode character varying, latitude double precision, longitude double precision, target_type character varying, distance double precision)
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE
    ROWS 1000

AS $BODY$
BEGIN
RETURN QUERY
SELECT lc.id, lc.created_at, lc.created_by, lc.updated_at, lc.updated_by, lc.address, lc.city,
       lc.country, lc.state, lc.zipcode, lc.latitude, lc.longitude, lc.target_type,
       func_get_distance_from_lat_lng_in_km(lat, lng, lc.latitude, lc.longitude) AS distance
FROM location lc
WHERE lc.target_type = targetType
ORDER BY distance ASC
    LIMIT dLimit
OFFSET dOffset;
RETURN;   -- there will not be any next result, finish execution
END $BODY$;

ALTER FUNCTION public.func_get_nearest_locations(double precision, double precision, character varying, integer, integer, character varying)
    OWNER TO postgres;