# 行业指数：消费，医药

# 1,医药
SELECT * FROM t_index i WHERE (NAME LIKE '%医药%')
	and EXISTS (SELECT 1 FROM t_index_fund f WHERE f.index_code=i.code);

SELECT * FROM t_index_fund WHERE index_code IN('000037', '000814', '000933', '000991');

SELECT * FROM t_fund WHERE NAME LIKE '%医药%' 
	ORDER BY gm DESC, TYPE;
	
SELECT f.code, f.name, f.type, f.gm, f.company, f.setup_date, ts.track_diff, ts.track_index FROM t_fund f
	LEFT JOIN t_fund_ts ts ON f.code=ts.code
	WHERE f.CODE IN (
		SELECT code FROM t_fund_ts wHERE track_index LIKE '%医药%' )
	AND f.setup_date<'2016-1-1' AND f.gm>2
	ORDER BY f.type, f.setup_date ASC, f.gm DESC;
	
SELECT f.code, f.name, f.type, f.gm, f.company, f.setup_date, ts.track_diff, ts.track_index FROM t_fund f
	LEFT JOIN t_fund_ts ts ON f.code=ts.code
	WHERE (f.name LIKE '%医药%' OR f.name LIKE '%医疗%')
 	AND f.setup_date>'2016-1-1' AND f.gm>5
	ORDER BY f.type, f.setup_date ASC, f.gm DESC;
-- -----------------------------------------------

# 2,消费
SELECT * FROM t_index i WHERE (NAME LIKE '%白酒%')
	and EXISTS (SELECT 1 FROM t_index_fund f WHERE f.index_code=i.code);
		
SELECT f.code, f.name, f.type, f.gm, f.company, f.setup_date, ts.track_diff, ts.track_index FROM t_fund f
	LEFT JOIN t_fund_ts ts ON f.code=ts.code
	WHERE (f.name LIKE '%消费%')
 	AND f.setup_date>'2016-1-1' AND f.gm>5
	ORDER BY f.type, f.setup_date ASC, f.gm DESC;
-- -----------------------------------------------

# 3，农业
SELECT * FROM t_index i WHERE (NAME LIKE '%农业%')
	and EXISTS (SELECT 1 FROM t_index_fund f WHERE f.index_code=i.code);
		
SELECT f.code, f.name, f.type, f.gm, f.company, f.setup_date, ts.track_diff, ts.track_index FROM t_fund f
	LEFT JOIN t_fund_ts ts ON f.code=ts.code
	WHERE (f.name LIKE '%农业%')
 	AND f.setup_date<'2018-1-1' AND f.gm>5
	ORDER BY f.type, f.setup_date ASC, f.gm DESC;
	
-- -----------------------------------------------

# 4，新能源
SELECT * FROM t_index i WHERE (NAME LIKE '%新能源%')
	and EXISTS (SELECT 1 FROM t_index_fund f WHERE f.index_code=i.code);
		
SELECT f.code, f.name, f.type, f.gm, f.company, f.setup_date, ts.track_diff, ts.track_index FROM t_fund f
	LEFT JOIN t_fund_ts ts ON f.code=ts.code
	WHERE (f.name LIKE '%新能源%' OR f.name LIKE '%光伏%')
 	AND f.setup_date<'2021-1-1' AND f.gm>5
	ORDER BY f.type, f.setup_date ASC, f.gm DESC;
	
SELECT f.code, f.name, f.type, f.gm, f.company, f.setup_date, ts.track_diff, ts.track_index FROM t_fund f
	LEFT JOIN t_fund_ts ts ON f.code=ts.code
	WHERE (f.name LIKE '%汽车%')
 	AND f.setup_date<'2021-1-1' AND f.gm>5
	ORDER BY f.type, f.setup_date ASC, f.gm DESC;	
-- -----------------------------------------------

# 5，工业，高端制造
SELECT * FROM t_index i WHERE (NAME LIKE '%高端%' OR NAME LIKE '%工业%' OR NAME LIKE '%制造%')
	and EXISTS (SELECT 1 FROM t_index_fund f WHERE f.index_code=i.code);
	
SELECT * FROM t_index_fund WHERE index_code IN('399803', '930820', '930850');
		
SELECT f.code, f.name, f.type, f.gm, f.company, f.setup_date, ts.track_diff, ts.track_index FROM t_fund f
	LEFT JOIN t_fund_ts ts ON f.code=ts.code
	WHERE (f.name LIKE '%工业%' OR f.name LIKE '%制造%' OR f.name LIKE '%高端%')
 	AND f.setup_date<'2019-1-1' AND f.gm>5
	ORDER BY f.type, f.setup_date ASC, f.gm DESC;
	 
	
	
	