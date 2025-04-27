INSERT INTO funcionario (id, nome, email, senha) VALUES (11, 'Ana Admin', 'ana@admin.com', 'JDJhJDEwJGswZUNxdkxDc0taWjU2ZjJOWmVNZjBPdVk5TTRQUUU5M1BLZ0FxelA3RjQ2WHY5TGpPU2FCM3E=');
INSERT INTO funcionario (id, nome, email, senha) VALUES (21, 'João Porteiro', 'joao@condominio.com', 'JDJhJDEwJGswZUNxdkxDc0taWjU2ZjJOWmVNZjBPdVk5TTRQUUU5M1BLZ0FxelA3RjQ2WHY5TGpPU2FCM3E=');
INSERT INTO funcionario (id, nome, email, senha) VALUES (31, 'Carlos Porteiro', 'carlos@condominio.com', 'JDJhJDEwJGswZUNxdkxDc0taWjU2ZjJOWmVNZjBPdVk5TTRQUUU5M1BLZ0FxelA3RjQ2WHY5TGpPU2FCM3E=');


INSERT INTO funcionario_roles (funcionario_id, role) VALUES (11, 'ROLE_ADMIN');

INSERT INTO funcionario_roles (funcionario_id, role) VALUES (21, 'ROLE_PORTEIRO');

INSERT INTO funcionario_roles (funcionario_id, role) VALUES (31, 'ROLE_PORTEIRO');

INSERT INTO morador (id, nome, email, senha, telefone, apartamento) VALUES (11, 'Clara Residente', 'clara@residencial.com', 'JDJhJDEwJGswZUNxdkxDc0taWjU2ZjJOWmVNZjBPdVk5TTRQUUU5M1BLZ0FxelA3RjQ2WHY5TGpPU2FCM3E=', '11987654321', '101');
INSERT INTO morador (id, nome, email, senha, telefone, apartamento) VALUES (21, 'Marcos Silva', 'marcos@residencial.com', 'JDJhJDEwJGswZUNxdkxDc0taWjU2ZjJOWmVNZjBPdVk5TTRQUUU5M1BLZ0FxelA3RjQ2WHY5TGpPU2FCM3E=', '11912345678', '102');
INSERT INTO morador (id, nome, email, senha, telefone, apartamento) VALUES (31, 'Fernanda Souza', 'fernanda@residencial.com', 'JDJhJDEwJGswZUNxdkxDc0taWjU2ZjJOWmVNZjBPdVk5TTRQUUU5M1BLZ0FxelA3RjQ2WHY5TGpPU2FCM3E=', '11998765432', '103');
INSERT INTO morador (id, nome, email, senha, telefone, apartamento) VALUES (41, 'Bruno Oliveira', 'bruno@residencial.com', 'JDJhJDEwJGswZUNxdkxDc0taWjU2ZjJOWmVNZjBPdVk5TTRQUUU5M1BLZ0FxelA3RjQ2WHY5TGpPU2FCM3E=', '11945678901', '104');


INSERT INTO morador_roles (morador_id, role) VALUES (11, 'ROLE_MORADOR');

INSERT INTO morador_roles (morador_id, role) VALUES (21, 'ROLE_MORADOR');

INSERT INTO morador_roles (morador_id, role) VALUES (31, 'ROLE_MORADOR');

INSERT INTO morador_roles (morador_id, role) VALUES (41, 'ROLE_MORADOR');