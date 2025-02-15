INSERT INTO department (name)
VALUES
    ('Бухгалтерия'),
    ('Маркетинг'),
    ('Лудоманы');

INSERT INTO employee (name, salary, department_id, is_manager)
VALUES
    ('Анна Козлова', 80000, 1, false),
    ('Сергей Сидоров', 60000, 2, true),
    ('Ольга Дмитриева', 50000, 3, false),
    ('Дмитрий Степанов', 700.5, 1, false);