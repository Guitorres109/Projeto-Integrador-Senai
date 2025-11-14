let usuario;
let email;
let senha;
let confirmarsenha = document.getElementById('confirmarsenha').value;
let tipo;

function enviarDados() {
  usuario = document.getElementById('usuario').value.trim();
  email = document.getElementById('email').value;
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
  if(senha != confirmarsenha){
    alert("As senhas não correspondem");
    return;
  }
  const url = new URL(window.location.href);
      url.searchParams.set('usuario', usuario);
      url.searchParams.set('email', email);
      url.searchParams.set('senha', senha);
      url.searchParams.set('tipo', tipo);
      window.history.pushState({}, '', url);
}