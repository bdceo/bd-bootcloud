# 策略指数：低波动、基本面、红利、价值

# 1,低波动
SELECT * FROM t_index i WHERE (NAME LIKE '%波动%' OR NAME LIKE '%LV%')
	and EXISTS (SELECT 1 FROM t_index_fund f WHERE f.index_code=i.code);

SELECT * FROM t_index_fund WHERE index_code IN('930740', '930782', '930846', '931157');

SELECT * FROM t_fund WHERE NAME LIKE '%波动%' 
	ORDER BY gm DESC, TYPE;
	
SELECT * FROM t_fund WHERE CODE IN (SELECT code FROM t_index_fund WHERE index_code IN('930740', '930782', '930846', '931157'))
	AND gm>2
	ORDER BY gm DESC, TYPE; 
-- -----------------------------------------------


# 2，红利
SELECT * FROM t_index i WHERE (NAME LIKE '%红利%')
	and EXISTS (SELECT 1 FROM t_index_fund f WHERE f.index_code=i.code);

SELECT * FROM t_index_fund WHERE index_code IN('000015', '000821', '000922', '930740', '930955', '931157', 'H30089', 'H30269');

SELECT * FROM t_fund WHERE NAME LIKE '%红利%' 
	ORDER BY gm DESC, TYPE;
	
SELECT * FROM t_fund WHERE CODE IN (SELECT code FROM t_index_fund WHERE index_code IN('000015', '000821', '000922', '930740', '930955', '931157', 'H30089', 'H30269'))
	AND gm>2 
	ORDER BY type, setup_date ASC, gm DESC;
SELECT * FROM t_fund WHERE CODE IN (SELECT code FROM t_index_fund WHERE index_code IN('000015', '000821', '000922', '930740', '930955', '931157', 'H30089', 'H30269'))
	AND gm>2 AND setup_date<'2016-2-20'
	ORDER BY type, setup_date ASC, gm DESC;
-- -----------------------------------------------


# 3，基本面
SELECT * FROM t_index i WHERE (NAME LIKE '%基本面%')
	and EXISTS (SELECT 1 FROM t_index_fund f WHERE f.index_code=i.code);

SELECT * FROM t_index_fund WHERE index_code IN('000925');

SELECT * FROM t_fund WHERE NAME LIKE '%基本面%' 
	ORDER BY gm DESC, TYPE;
	 
SELECT * FROM t_fund WHERE NAME LIKE '%基本面%'
	AND gm>2 AND setup_date<'2016-2-20'
	ORDER BY type, setup_date ASC, gm DESC;
-- -----------------------------------------------


# 4，价值
SELECT * FROM t_index i WHERE (NAME LIKE '%价值%')
	and EXISTS (SELECT 1 FROM t_index_fund f WHERE f.index_code=i.code);

SELECT * FROM t_index_fund WHERE index_code IN('000029', '000919');

SELECT * FROM t_fund WHERE NAME LIKE '%价值%' 
	ORDER BY gm DESC, TYPE;
	 
SELECT * FROM t_fund WHERE NAME LIKE '%价值%'
	AND gm>5 AND setup_date<'2016-2-20'
	ORDER BY type, setup_date ASC, gm DESC;
-- -----------------------------------------------

# 5，成长
SELECT * FROM t_index i WHERE (NAME LIKE '%成长%')
	and EXISTS (SELECT 1 FROM t_index_fund f WHERE f.index_code=i.code);

SELECT * FROM t_index_fund WHERE index_code IN('931157');

SELECT * FROM t_fund WHERE NAME LIKE '%成长%' 
	ORDER BY gm DESC, TYPE;
	 
SELECT * FROM t_fund WHERE NAME LIKE '%成长%'
	AND gm>5 AND setup_date<'2016-2-20'
	ORDER BY type, setup_date ASC, gm DESC;
 
SELECT f.code, f.name, f.type, f.gm, f.company, f.setup_date, ts.track_diff, ts.track_index 
	FROM t_fund f
	LEFT JOIN t_fund_info i ON i.code=f.code
	LEFT JOIN t_fund_ts ts ON f.code=ts.code
	LEFT JOIN t_company c ON f.company_code=c.code
	WHERE f.NAME LIKE '%成长%' 
	AND i.target LIKE '%成长%'
	AND c.fund_gm>1000
	AND f.gm>5 AND f.setup_date<'2016-2-20'
	ORDER BY f.TYPE, f.setup_date ASC, f.gm DESC;

-- -----------------------------------------------

# 6，趋势
SELECT * FROM t_index i WHERE (NAME LIKE '%趋势%')
	and EXISTS (SELECT 1 FROM t_index_fund f WHERE f.index_code=i.code);
 
SELECT f.code, f.name, f.type, f.gm, f.company, f.setup_date, ts.track_diff, ts.track_index 
	FROM t_fund f
	LEFT JOIN t_fund_info i ON i.code=f.code
	LEFT JOIN t_fund_ts ts ON f.code=ts.code
	LEFT JOIN t_company c ON f.company_code=c.code
	WHERE f.NAME LIKE '%趋势%'
	AND c.fund_gm>1000
	AND f.gm>5 AND f.setup_date<'2018-2-20'
	ORDER BY f.TYPE, f.setup_date ASC, f.gm DESC;