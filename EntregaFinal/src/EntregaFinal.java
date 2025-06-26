
// EntregaFinal.java

import java.sql.*;
import java.util.*;

public class EntregaFinal {
    private static final String URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String USER = "root";
    private static final String PASS = "Francisco@1"; // Não sei se as nossas senhas estão iguais, essa é a minha.

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            while (true) {
                System.out.println("\nMENU PRINCIPAL:");
                System.out.println("1. Insert");
                System.out.println("2. Update");
                System.out.println("3. Delete");
                System.out.println("4. Select");
                System.out.println("5. Sair");
                System.out.print("Escolha: ");

                int op = Integer.parseInt(scanner.nextLine());
                switch (op) {
                    case 1 -> menuInsert(conn);
                    case 2 -> menuUpdate(conn);
                    case 3 -> menuDelete(conn);
                    case 4 -> menuSelect(conn);
                    case 5 -> System.exit(0);
                    default -> System.out.println("Opção inválida.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void menuInsert(Connection conn) throws SQLException {
        System.out.println("\nINSERT - Escolha a tabela:");
        System.out.println("1. Usuarios\n2. Locais\n3. Categorias\n4. Eventos\n5. Ingressos");
        int escolha = Integer.parseInt(scanner.nextLine());

        switch (escolha) {
            case 1 -> {
                System.out.print("Nome: "); String nome = scanner.nextLine();
                System.out.print("Email: "); String email = scanner.nextLine();
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO Usuarios (nome, email) VALUES (?, ?)");
                stmt.setString(1, nome); stmt.setString(2, email); stmt.executeUpdate();
            }
            case 2 -> {
                System.out.print("Nome: "); String nome = scanner.nextLine();
                System.out.print("Endereço: "); String endereco = scanner.nextLine();
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO Locais (nome, endereco) VALUES (?, ?)");
                stmt.setString(1, nome); stmt.setString(2, endereco); stmt.executeUpdate();
            }
            case 3 -> {
                System.out.print("Nome da categoria: "); String nome = scanner.nextLine();
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO Categorias (nome) VALUES (?)");
                stmt.setString(1, nome); stmt.executeUpdate();
            }
            case 4 -> {
                System.out.print("Nome do evento: "); String nome = scanner.nextLine();
                System.out.print("Data (YYYY-MM-DD): "); String data = scanner.nextLine();
                System.out.print("ID do local: "); int idLocal = Integer.parseInt(scanner.nextLine());
                System.out.print("ID da categoria: "); int idCategoria = Integer.parseInt(scanner.nextLine());
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO Eventos (nome, data, id_local, id_categoria) VALUES (?, ?, ?, ?)");
                stmt.setString(1, nome); stmt.setString(2, data); stmt.setInt(3, idLocal); stmt.setInt(4, idCategoria); stmt.executeUpdate();
            }
            case 5 -> {
                System.out.print("ID do evento: "); int evento = Integer.parseInt(scanner.nextLine());
                System.out.print("ID do usuário: "); int usuario = Integer.parseInt(scanner.nextLine());
                System.out.print("Preço: "); double preco = Double.parseDouble(scanner.nextLine());
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO Ingressos (id_evento, id_usuario, preco) VALUES (?, ?, ?)");
                stmt.setInt(1, evento); stmt.setInt(2, usuario); stmt.setDouble(3, preco); stmt.executeUpdate();
            }
            default -> System.out.println("Opção inválida.");
        }
        System.out.println("Inserção realizada com sucesso.");
    }

    private static void menuUpdate(Connection conn) throws SQLException {
        System.out.print("Digite a tabela a ser atualizada (Usuarios/Categorias/Locais/Eventos/Ingressos): ");
        String tabela = scanner.nextLine();
        System.out.print("Digite o ID do registro a ser atualizado: ");
        int id = Integer.parseInt(scanner.nextLine());

        if (tabela.equalsIgnoreCase("Usuarios")) {
            System.out.print("Novo nome: "); String nome = scanner.nextLine();
            System.out.print("Novo email: "); String email = scanner.nextLine();
            PreparedStatement stmt = conn.prepareStatement("UPDATE Usuarios SET nome = ?, email = ? WHERE id_usuario = ?");
            stmt.setString(1, nome); stmt.setString(2, email); stmt.setInt(3, id); stmt.executeUpdate();
        } else if (tabela.equalsIgnoreCase("Categorias")) {
            System.out.print("Novo nome: "); String nome = scanner.nextLine();
            PreparedStatement stmt = conn.prepareStatement("UPDATE Categorias SET nome = ? WHERE id_categoria = ?");
            stmt.setString(1, nome); stmt.setInt(2, id); stmt.executeUpdate();
        } else {
            System.out.println("Update para essa tabela ainda não implementado.");
        }
    }

    private static void menuDelete(Connection conn) throws SQLException {
        System.out.print("Digite a tabela da qual deseja excluir (Usuarios/Categorias/Locais/Eventos/Ingressos): ");
        String tabela = scanner.nextLine();
        System.out.print("Digite o ID do registro a ser excluído: ");
        int id = Integer.parseInt(scanner.nextLine());

        String campo = switch (tabela.toLowerCase()) {
            case "usuarios" -> "id_usuario";
            case "categorias" -> "id_categoria";
            case "locais" -> "id_local";
            case "eventos" -> "id_evento";
            case "ingressos" -> "id_ingresso";
            default -> null;
        };
        if (campo == null) {
            System.out.println("Tabela inválida.");
            return;
        }
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + tabela + " WHERE " + campo + " = ?");
        stmt.setInt(1, id); stmt.executeUpdate();
        System.out.println("Registro excluído com sucesso.");
    }

    private static void menuSelect(Connection conn) throws SQLException {
        System.out.print("Digite a tabela para SELECT (Usuarios/Categorias/Locais/Eventos/Ingressos): ");
        String tabela = scanner.nextLine();
        System.out.println("1. Ver todos os registros\n2. Buscar por ID\n3. JOIN com outra tabela");
        int escolha = Integer.parseInt(scanner.nextLine());

        if (escolha == 1) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tabela);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                for (int i = 1; i <= meta.getColumnCount(); i++)
                    System.out.print(meta.getColumnLabel(i) + ": " + rs.getString(i) + "  ");
                System.out.println();
            }
        } else if (escolha == 2) {
            System.out.print("Digite o ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            String campoID = switch (tabela.toLowerCase()) {
                case "usuarios" -> "id_usuario";
                case "categorias" -> "id_categoria";
                case "locais" -> "id_local";
                case "eventos" -> "id_evento";
                case "ingressos" -> "id_ingresso";
                default -> null;
            };
            if (campoID != null) {
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + tabela + " WHERE " + campoID + " = ?");
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                ResultSetMetaData meta = rs.getMetaData();
                while (rs.next()) {
                    for (int i = 1; i <= meta.getColumnCount(); i++)
                        System.out.print(meta.getColumnLabel(i) + ": " + rs.getString(i) + "  ");
                    System.out.println();
                }
            }
        } else if (escolha == 3) {
            System.out.println("Executando JOIN exemplo com Ingressos + Usuarios + Eventos...");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT u.nome AS usuario, e.nome AS evento, i.preco FROM Ingressos i JOIN Usuarios u ON i.id_usuario = u.id_usuario JOIN Eventos e ON i.id_evento = e.id_evento");
            while (rs.next()) {
                System.out.println("Usuário: " + rs.getString("usuario") + " | Evento: " + rs.getString("evento") + " | Preço: R$ " + rs.getDouble("preco"));
            }
        }
    }
}
