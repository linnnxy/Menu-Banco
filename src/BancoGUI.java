import javax.swing.*;
import java.awt.*;

public class BancoGUI extends JFrame {

    private Conta conta;

    private JLabel labelSaldo;
    private JTextField campoValor;

    public BancoGUI(Conta conta) {
        this.conta = conta;

        setTitle("Banco Digital Moderno");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel painelInfo = new JPanel(new GridLayout(2, 1));
        painelInfo.setBorder(BorderFactory.createTitledBorder("Dados do Cliente"));

        JLabel labelInfoCliente = new JLabel(String.format("<html><b>Nome:</b> %s<br><b>Conta:</b> %s</html>",
                conta.getNomeCliente(), conta.getTipoConta()));
        labelInfoCliente.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        labelInfoCliente.setFont(new Font("Arial", Font.PLAIN, 14));

        labelSaldo = new JLabel();
        labelSaldo.setFont(new Font("Arial", Font.BOLD, 16));
        labelSaldo.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        painelInfo.add(labelInfoCliente);
        painelInfo.add(labelSaldo);
        add(painelInfo, BorderLayout.NORTH);

        JPanel painelAcoes = new JPanel();
        painelAcoes.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelAcoes.setBorder(BorderFactory.createTitledBorder("Operações"));

        painelAcoes.add(new JLabel("Valor: R$"));
        campoValor = new JTextField(10);
        painelAcoes.add(campoValor);

        JButton btnDepositar = new JButton("Depositar");
        JButton btnTransferir = new JButton("Transferir");
        painelAcoes.add(btnDepositar);
        painelAcoes.add(btnTransferir);

        add(painelAcoes, BorderLayout.CENTER);

        JPanel painelControle = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSair = new JButton("Sair");
        painelControle.add(btnSair);
        add(painelControle, BorderLayout.SOUTH);

        atualizarLabelSaldo();

        btnDepositar.addActionListener(e -> realizarOperacao(true));
        btnTransferir.addActionListener(e -> realizarOperacao(false));

        btnSair.addActionListener(e -> System.exit(0));
    }

    private void realizarOperacao(boolean ehDeposito) {
        String valorStr = campoValor.getText().replace(",", ".");
        try {
            double valor = Double.parseDouble(valorStr);
            boolean sucesso;
            String operacao;

            if (ehDeposito) {
                sucesso = conta.depositar(valor);
                operacao = "Depósito";
            } else {
                sucesso = conta.transferir(valor);
                operacao = "Transferência";
            }

            if (sucesso) {
                JOptionPane.showMessageDialog(this, operacao + " realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarLabelSaldo();
            } else {
                JOptionPane.showMessageDialog(this, "Valor inválido ou saldo insuficiente.", "Erro na Operação", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, digite um valor numérico válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } finally {
            campoValor.setText("");
        }
    }

    private void atualizarLabelSaldo() {
        labelSaldo.setText(String.format("<html>Saldo Atual: <font color='green'><b>R$ %.2f</b></font></html>", conta.getSaldo()));
    }



    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String nomeDoUsuario;
        do {
            nomeDoUsuario = JOptionPane.showInputDialog(null, "Por favor, digite seu nome para continuar:", "Bem-vindo ao Banco Digital", JOptionPane.QUESTION_MESSAGE);
            if (nomeDoUsuario == null) System.exit(0);
            if (nomeDoUsuario.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "O nome não pode ser vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } while (nomeDoUsuario.trim().isEmpty());

        Conta contaDoUsuario = new Conta(nomeDoUsuario, "Corrente", 2500.00);

        SwingUtilities.invokeLater(() -> {
            BancoGUI interfaceGrafica = new BancoGUI(contaDoUsuario);
            interfaceGrafica.setVisible(true);
        });
    }
}