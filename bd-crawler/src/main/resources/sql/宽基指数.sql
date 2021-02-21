# 找跟踪的宽基指数

# 1,创业板
SELECT * FROM t_index i WHERE NAME LIKE '%创业%';

SELECT * FROM t_index_fund WHERE index_code='000958';

SELECT * FROM t_fund WHERE NAME LIKE '%创业%' 
	ORDER BY gm DESC, TYPE;
	
# 特色数据：跟踪指数、跟踪误差
SELECT * FROM t_fund_ts 
	WHERE track_index LIKE '%创业%' AND track_diff>0 
	AND std_dev1>0 AND std_dev2>0 AND std_dev3>0
	ORDER BY track_index, track_diff ASC, std_dev1 asc;
	
# 对应产品：成立时间、基金规模
SELECT f.`*`, ts.track_diff, ts.track_index FROM t_fund f
	LEFT JOIN t_fund_ts ts ON f.code=ts.code
	WHERE f.CODE IN (
		SELECT code FROM t_fund_ts 
			WHERE track_index LIKE '%创业%' AND track_diff>0 
			AND std_dev1>0 AND std_dev2>0 AND std_dev3>0)
	AND f.setup_date<'2016-12-31' AND f.gm>10
	ORDER BY f.setup_date ASC, f.gm DESC;
-- -----------------------------------------------	


# 2,上证50
SELECT * FROM t_index i WHERE NAME LIKE '%上证50%';

SELECT * FROM t_index_fund WHERE index_code='000016';

SELECT * FROM t_fund WHERE NAME LIKE '%上证50%' 
	ORDER BY gm DESC, TYPE;
	
# 特色数据：跟踪指数、跟踪误差
SELECT * FROM t_fund_ts 
	WHERE track_index LIKE '%上证50%' AND track_diff>0 
	AND std_dev1>0 AND std_dev2>0 AND std_dev3>0
	ORDER BY track_index, track_diff ASC, std_dev1 asc;
	
# 对应产品：成立时间、基金规模
SELECT f.`*`, ts.track_diff, ts.track_index FROM t_fund f
	LEFT JOIN t_fund_ts ts ON f.code=ts.code
	WHERE f.CODE IN (
		SELECT code FROM t_fund_ts 
			WHERE track_index LIKE '%上证50%' AND track_diff>0 
			AND std_dev1>0 AND std_dev2>0 AND std_dev3>0)
	AND f.setup_date<'2015-12-31' AND f.gm>10
	ORDER BY f.setup_date ASC, f.gm DESC;
-- -----------------------------------------------	
	

# 3,沪深300
SELECT * FROM t_index i WHERE NAME LIKE '%沪深300%';

SELECT * FROM t_index_fund WHERE index_code='000300';

SELECT * FROM t_fund WHERE NAME LIKE '%沪深300%' 
	ORDER BY gm DESC, TYPE;
	
# 特色数据：跟踪指数、跟踪误差
SELECT * FROM t_fund_ts 
	WHERE track_index LIKE '%沪深300%' AND track_diff>0 
	AND std_dev1>0 AND std_dev2>0 AND std_dev3>0
	ORDER BY track_index, track_diff ASC, std_dev1 asc;
	
# 对应产品：成立时间、基金规模
SELECT f.`*`, ts.track_diff, ts.track_index FROM t_fund f
	LEFT JOIN t_fund_ts ts ON f.code=ts.code
	WHERE f.CODE IN (
		SELECT code FROM t_fund_ts 
			WHERE track_index='沪深300指数' AND track_diff>0 
			AND std_dev1>0 AND std_dev2>0 AND std_dev3>0)
	AND f.setup_date<'2016-2-20' AND f.gm>20
	ORDER BY f.type, f.setup_date ASC, f.gm DESC;
-- -----------------------------------------------

# 4,中证500
SELECT * FROM t_index i WHERE NAME LIKE '%中证500%';

SELECT * FROM t_index_fund WHERE index_code='000905';

SELECT * FROM t_fund WHERE NAME LIKE '%中证500%' 
	ORDER BY gm DESC, TYPE;
	
# 特色数据：跟踪指数、跟踪误差
SELECT * FROM t_fund_ts 
	WHERE track_index LIKE '%中证500%' AND track_diff>0 
	AND std_dev1>0 AND std_dev2>0 AND std_dev3>0
	ORDER BY track_index, track_diff ASC, std_dev1 asc;
	
# 对应产品：成立时间、基金规模
SELECT f.`*`, ts.track_diff, ts.track_index FROM t_fund f
	LEFT JOIN t_fund_ts ts ON f.code=ts.code
	WHERE f.CODE IN (
		SELECT code FROM t_fund_ts 
			WHERE track_index='中证500指数' AND track_diff>0 
			AND std_dev1>0 AND std_dev2>0 AND std_dev3>0)
	AND f.setup_date<'2016-2-20' AND f.gm>20
	ORDER BY f.type, f.setup_date ASC, f.gm DESC;
-- -----------------------------------------------	


# 5,科创板
SELECT * FROM t_index i WHERE NAME LIKE '%科创%'; 

SELECT * FROM t_index_fund WHERE index_code='000688';

SELECT * FROM t_fund WHERE NAME LIKE '%科创%' 
	ORDER BY gm DESC, TYPE;
	
# 特色数据：跟踪指数、跟踪误差
SELECT * FROM t_fund_ts
	WHERE track_index LIKE '%科创%' 
	ORDER BY track_index, track_diff ASC, std_dev1 ASC;
	
# 对应产品：成立时间、基金规模
SELECT f.`*`  FROM t_fund f
	WHERE f.NAME LIKE '%科创%' AND f.gm>10
	ORDER BY f.type, f.setup_date ASC, f.gm DESC;
				
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	