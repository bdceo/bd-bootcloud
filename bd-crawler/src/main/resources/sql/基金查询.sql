

SELECT * FROM t_index WHERE NAME LIKE 'ESG%'


SELECT  id,code,dt,stock_code,stock_name,value_rate,stocks,values  FROM t_fund_stock


SELECT * FROM t_fund_val WHERE CODE='162907';


SELECT id, dt, day_growth FROM t_fund_val LIMIT 1,5000;


SELECT * FROM t_fund WHERE CODE='160632';
SELECT * FROM t_fund_ts WHERE CODE='159949';


SELECT * FROM t_fund 
WHERE setup_date IS NOT null
ORDER BY gm asc, setup_date

LIMIT 10;


SELECT * from t_fund WHERE CODE NOT IN(
SELECT CODE FROM t_fund_val GROUP BY CODE)


SELECT '基金总数', COUNT(*) FROM t_fund
UNION
SELECT '经理变动', COUNT(DISTINCT(CODE)) FROM t_fund_manager
UNION 
SELECT '股票持仓', COUNT(DISTINCT(CODE)) FROM t_fund_stock
UNION 
SELECT '债券持仓', COUNT(DISTINCT(CODE)) FROM t_fund_bond
UNION
SELECT '净值历史', COUNT(DISTINCT(CODE)) FROM t_fund_val


