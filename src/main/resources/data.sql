INSERT INTO equipment (name, category, status) VALUES ('ノートPC A', 'PC', 'AVAILABLE');
INSERT INTO equipment (name, category, status) VALUES ('ノートPC B', 'PC', 'AVAILABLE');
INSERT INTO equipment (name, category, status) VALUES ('プロジェクター', 'AV', 'AVAILABLE');
INSERT INTO equipment (name, category, status) VALUES ('Webカメラ', 'AV', 'LENT');
INSERT INTO equipment (name, category, status) VALUES ('古いモニター', 'DISPLAY', 'RETIRED');
INSERT INTO equipment (name, category, status) VALUES ('4Kモニター', 'DISPLAY', 'AVAILABLE');

INSERT INTO loan (equipment_id, borrower, lent_on, due_on) VALUES (4, 'sato', DATE '2026-10-01', DATE '2026-10-08');
