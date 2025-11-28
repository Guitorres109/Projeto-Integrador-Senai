let usuario;
let senha;
let tipo;
let respostaDiv;

function togglePassword() {
  const senhaInput = document.getElementById("senha");
  senhaInput.type = senhaInput.type === "password" ? "text" : "password";
  document.getElementById("botaosenha").classList.toggle("ativo");
}
function togglePassword2() {
  const senhaInput = document.getElementById("confirmarsenha");
  senhaInput.type = senhaInput.type === "password" ? "text" : "password";
  document.getElementById("botaoconfirmarsenha").classList.toggle("ativo");
}

// Exibir alerta personalizado
function showAlert(mensagem) {
  const alertBox = document.getElementById('customAlert');
  const alertMessage = document.getElementById('alertMessage');
  alertMessage.textContent = mensagem;
  alertBox.style.display = 'block';
}

// Esconder alerta
function hideAlert() {
  document.getElementById('customAlert').style.display = 'none';
}

// Atualizar exibição com base na query string
function atualizarExibicao() {
  const params = new URLSearchParams(window.location.search);
  document.getElementById('usuario').value = params.get('usuario') || '';
  document.getElementById('senha').value = params.get('senha') || '';
  document.getElementById('tipo').value = params.get('tipo') || '';
}

// Enviar dados e atualizar a URL
function enviarDados() {
  usuario = document.getElementById('usuario').value.trim();
  senha = document.getElementById('senha').value;
  tipo = document.getElementById('tipo').value;

  if (!usuario || !senha) {
    showAlert('Por favor, preencha o nome e a senha.');
    return;
  }

  if (!tipo) {
    showAlert('Por favor, selecione um perfil de usuário.');
    return;
  }
  const url = new URL(window.location.href);
      url.searchParams.set('usuario', usuario);
      url.searchParams.set('senha', senha);
      url.searchParams.set('tipo', tipo);
      window.history.pushState({}, '', url);

      respostaDiv.textContent = "Dados enviados com sucesso!";

  // Se quiser fazer algo com esses dados, coloque aqui.
}
function toggleTheme() {
    document.body.classList.toggle("dark-mode");

    // salvar preferência
    if (document.body.classList.contains("dark-mode")) {
        localStorage.setItem("tema", "escuro");
    } else {
        localStorage.setItem("tema", "claro");
    }
    const darkModeButton = document.querySelector('.dark-mode-button');
}

// botão
document.getElementById("toggle-theme").addEventListener("click", toggleTheme);

// carregar preferencia ao abrir a página
if (localStorage.getItem("tema") === "escuro") {
    document.body.classList.add("dark-mode");
}


// Atualiza a exibição ao carregar a página
window.onload = atualizarExibicao;

// Animação "reveal"
const items = document.querySelectorAll('.reveal');

const observer = new IntersectionObserver(entries => {
  entries.forEach(entry => {
    if (entry.isIntersecting) {
        entry.target.classList.add('active');
        observer.unobserve(entry.target); // só uma vez
    }
  });
}, { threshold: 0.5 });

items.forEach(item => observer.observe(item));
// Função para alternar a visibilidade do bloco e da sobreposição
function toggleVisibility(buttonId, elementId, overlayId, action) {
    const button = document.getElementById(buttonId);
    const element = document.getElementById(elementId);
    const overlay = document.getElementById(overlayId);

    // Verifica se o botão, o elemento e a sobreposição existem
    if (!button || !element || !overlay) {
        console.log("Botão, elemento ou sobreposição não encontrado!");
        return;
    }

    // Adiciona o evento de clique no botão
    button.addEventListener('click', function() {
        if (action === 'show') {
            // Exibe o bloco e a sobreposição
            element.style.display = 'flex';
            overlay.style.display = 'block';
        } else if (action === 'hide') {
            // Esconde o bloco e a sobreposição
            element.style.display = 'none';
            overlay.style.display = 'none';
        }
    });
}

// Aguarda o carregamento completo do DOM
document.addEventListener('DOMContentLoaded', function() {
    // Chama a função para mostrar o bloco e a sobreposição ao clicar no primeiro botão
    toggleVisibility('mostrar-bloco', 'bloco-atividade', 'overlay', 'show');

    // Chama a função para esconder o bloco e a sobreposição ao clicar no botão "Voltar"
    toggleVisibility('voltar-bloco', 'bloco-atividade', 'overlay', 'hide');
});

// Pega o botão e o bloco-header
const botao = document.querySelector('.feito');
const blocoHeader = document.querySelector('.bloco-header');

// Quando o botão for clicado, alternamos a classe 'mostrar'
botao.addEventListener('click', () => {
    // Adiciona ou remove a classe 'mostrar' no botão
    botao.classList.toggle('mostrar');
    
    // A alteração do estilo do bloco-header já é tratada pelo CSS com a classe 'mostrar'
});

