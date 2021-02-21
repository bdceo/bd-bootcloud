
# 1，2020年4季度，持仓排名

SELECT 
CONCAT('A-',a.stock_code) AS stock_code_1,
a.*,
case when b.stock_code IS NULL then '四季度新加入' ELSE '三季度有四季度继续持有' END AS is_in
,'四季度持有' AS TYPE1
,c.fund_num AS fund_num_3,c.market_value AS market_value_3
FROM 
(SELECT stock_code,stock_name,COUNT(CODE) as fund_num,round(SUM(market_value)/10000,2) AS market_value 
FROM t_fund_stock
WHERE dt = '2020-12-31'
GROUP BY stock_code,stock_name
ORDER BY SUM(market_value) DESC 
LIMIT 100) AS a 
LEFT JOIN 
(SELECT stock_code,stock_name,COUNT(CODE) as fund_num,round(SUM(market_value)/10000,2) AS market_value 
FROM t_fund_stock
WHERE dt = '2020-09-30'
GROUP BY stock_code,stock_name
ORDER BY SUM(market_value) DESC 
LIMIT 100) AS b ON a.stock_code = b.stock_code
LEFT JOIN 
(SELECT stock_code,stock_name,COUNT(CODE) as fund_num,round(SUM(market_value)/10000,2) AS market_value 
FROM t_fund_stock
WHERE dt = '2020-09-30'
GROUP BY stock_code,stock_name
ORDER BY SUM(market_value) DESC 
) AS c ON a.stock_code = c.stock_code

UNION ALL 

SELECT
CONCAT('A-',a.stock_code) AS stock_code_1, 
a.*,
case when b.stock_code IS NULL then '四季度出局' ELSE '三季度有四季度继续持有' END AS is_in
,'三季度持有' AS TYPE1
,c.fund_num AS fund_num_3,c.market_value AS market_value_3
FROM 
(SELECT stock_code,stock_name,COUNT(CODE) as fund_num,round(SUM(market_value)/10000,2) AS market_value 
FROM t_fund_stock
WHERE dt = '2020-09-30'
GROUP BY stock_code,stock_name
ORDER BY SUM(market_value) DESC 
LIMIT 100) AS a 
LEFT JOIN 
(SELECT stock_code,stock_name,COUNT(CODE) as fund_num,round(SUM(market_value)/10000,2) AS market_value 
FROM t_fund_stock
WHERE dt = '2020-12-31'
GROUP BY stock_code,stock_name
ORDER BY SUM(market_value) DESC 
LIMIT 100) AS b ON a.stock_code = b.stock_code
LEFT JOIN 
(SELECT stock_code,stock_name,COUNT(CODE) as fund_num,round(SUM(market_value)/10000,2) AS market_value 
FROM t_fund_stock
WHERE dt = '2020-12-31'
GROUP BY stock_code,stock_name
ORDER BY SUM(market_value) DESC 
) AS c ON a.stock_code = c.stock_code


