let usuarios = ["Guilherme Torres", "Nicolas Tordino", "João Pedro", "Pietro Pardim"];
// Mapear senhas para cada usuário
let senhas = {
  "Guilherme Torres": "12345678",
  "Nicolas Tordino": "12345678",
  "João Pedro": "12345678",
  "Pietro Pardim": "12345678"
};

let tipos_usuario = {
  "Guilherme Torres": "professor",
  "Nicolas Tordino": "aluno",
  "João Pedro": "aluno",
  "Pietro Pardim": "professor"
};

let input_usuario = document.getElementById('usuario'); // Corrigido de 'Usuario' para 'usuario'
let input_senha = document.getElementById('senha'); // Corrigido de 'Senha' para 'senha'

function verificar_usuario() {
    let usuario_digitado = input_usuario.value;
    let senha_digitada = input_senha.value;

    // Verifica se o usuário existe
    let usuario_existe = usuarios.includes(usuario_digitado);
    // Verifica se a senha corresponde ao usuário
    let senha_existe = senhas[usuario_digitado] === senha_digitada;

    if (usuario_existe && senha_existe) {
        // Login correto, vamos verificar o tipo de usuário
        let tipo_usuario = tipos_usuario[usuario_digitado];

        if (tipo_usuario === "professor") {
            alert("Bem-vindo, Professor " + usuario_digitado + "!");
            // Redireciona para a página do professor
            window.location.href = "acesso-professor.html"; // Página para professores
        } else if (tipo_usuario === "aluno") {
            alert("Bem-vindo, Aluno " + usuario_digitado + "!");
            // Redireciona para a página do aluno
            window.location.href = "Atividades.html"; // Página para alunos
        }
    } else {
        // Login incorreto, impede o avanço
        document.getElementById('customAlert').style.display = 'block'; // Exibe o alerta personalizado
        document.getElementById('alertMessage').innerText = "Usuário ou senha incorretos! Tente novamente.";
    }
}

// Função para esconder o alerta
function hideAlert() {
    document.getElementById('customAlert').style.display = 'none';
}
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