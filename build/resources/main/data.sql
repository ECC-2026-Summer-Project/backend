INSERT INTO company (company_id, company_name, sector, current_price, trend, volatility, dividend_yield, last_updated)
VALUES ('005930', '삼성전자', '반도체', 71500, 'UP', 0.15, 0.4, CURRENT_TIMESTAMP);

INSERT INTO company_info (company_id, market, description, listed_date, ceo, employees, is_ai_recommended, market_cap, per)
VALUES ('005930', 'KOSPI', '반도체 및 IT 기기 제조 기업', '1975-06-11', '한종희', 267900, true, 427000000000000, 15.8);

INSERT INTO dividend (company_id, dividend_year, amount_per_share, yield_rate)
VALUES ('005930', 2025, 850, 0.4);

INSERT INTO dividend (company_id, dividend_year, amount_per_share, yield_rate)
VALUES ('005930', 2024, 800, 0.38);

INSERT INTO trade_history (user_id, company_id, trade_type, quantity, price, traded_at)
VALUES (1, '005930', 'BUY', 10, 71500, CURRENT_TIMESTAMP);

INSERT INTO trade_history (user_id, company_id, trade_type, quantity, price, traded_at)
VALUES (1, '005930', 'SELL', 5, 71600, CURRENT_TIMESTAMP);