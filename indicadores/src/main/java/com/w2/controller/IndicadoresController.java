package com.w2.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.w2.model.Centro;
import com.w2.model.Horario;
import com.w2.model.Planta;
import com.w2.model.Predio;
import com.w2.model.Sala;
import com.w2.model.Semestre;
import com.w2.model.support.Ocupacao;
import com.w2.model.util.DiasSemana;
import com.w2.model.util.HorararioOrdem;
import com.w2.model.util.Mesorregioes;
import com.w2.model.util.TDHS;
import com.w2.model.util.Turnos;
import com.w2.repository.CentrosRepositorio;
import com.w2.repository.HorarioRepositorio;
import com.w2.repository.HorariosPadraoRepositorio;
import com.w2.repository.PlantasRepositorio;
import com.w2.repository.PrediosRepositorio;
import com.w2.repository.SalasRepositorio;
import com.w2.repository.SemestreRepositorio;

@Controller
@SessionAttributes(value = {"semestreContexto", "guka"})

public class IndicadoresController {

	@Autowired
	private PlantasRepositorio plantas;

	@Autowired
	private Environment env;

	@Autowired
	private HorariosPadraoRepositorio horarioPadrao;

	@Autowired
	private SemestreRepositorio semestres;

	@Autowired
	private CentrosRepositorio centros;

	@Autowired
	private PrediosRepositorio predios;

	@Autowired
	private SalasRepositorio salas;

	@Autowired
	private HorarioRepositorio horarios;

	@ModelAttribute("googleMapsAPIKey")
	public String googleApiKey() {		
		return env.getProperty("maps.api_key");
	}



	@RequestMapping("/login")
	public ModelAndView login() {

		ModelAndView modelView = new ModelAndView("Login");

		Semestre semestreAtual = semestres.findByAtivoIsTrue();

		if(semestreAtual == null) {

			semestreAtual = new Semestre();

		}


		modelView.addObject("semestreContexto", semestreAtual);

		return modelView;  
	}



	@RequestMapping("/")
	public ModelAndView index(@ModelAttribute("semestreContexto") Semestre  semestreContexto) 
	{ 


		ModelAndView modelView = new ModelAndView();

		if(semestreContexto.getIdSemestre() != 0) {


			List<Semestre> todosSemestres = semestres.findAllByOrderByIdSemestreDesc();

			modelView.setViewName("Index");
			List<Centro> todosCentros = centros.findAll();
			List<Predio> todosPredios = predios.findBySemestre(semestreContexto);


			modelView.addObject("centros", todosCentros);
			modelView.addObject("predios", todosPredios);
			modelView.addObject("semestres", todosSemestres);
			modelView.addObject("mesorregioes", Mesorregioes.values());


		} else {

			modelView.setViewName("redirect:/cadsemestre");

		}

		return modelView;
	}



	@RequestMapping(value="/mudarcontexto", method = RequestMethod.POST)
	public String mudarContextoSemestre(Model model, int idSemestre, @ModelAttribute("semestreContexto") Semestre semestreContexto, RedirectAttributes attributes, String urlAtual) {

		model.toString();


		Semestre semestreEscolhido = semestres.findOne(idSemestre);

		semestreContexto = semestreEscolhido;

		attributes.addFlashAttribute("semestreContexto", semestreEscolhido);
		return "redirect:/";
	}	






	@RequestMapping(value="/finalizarconfiguracao")
	public ModelAndView finalizarConfiguracao(Model model, RedirectAttributes attributes) {


		Semestre semestreContexto = new Semestre();

		semestreContexto = semestres.findByAtivoIsTrue();

		ModelAndView modelView  = new ModelAndView("redirect:/");

		modelView.addObject("semestreContexto", semestreContexto);

		attributes.addFlashAttribute("semestreContexto", semestreContexto);


		return modelView;
	}


	@RequestMapping(value="/predio/{codigo}")
	public ModelAndView exibirPredio(@PathVariable int codigo,  @ModelAttribute("semestreContexto") Semestre semestreContexto) {

		ModelAndView modelView = new ModelAndView("PredioDetalhes");

		Predio predio = predios.findByIdPredioAndSemestre(codigo, semestreContexto);
		modelView.addObject("predio", predio);

 
		return modelView;

	}

	@RequestMapping(value="/predio/{codigo}/tdhs")
	public ModelAndView  exibirTDHS(@PathVariable int codigo,  @ModelAttribute("semestreContexto") Semestre  semestreContexto) {
			

		Predio predio = predios.findOne(codigo);

		//Numero de salas do predio
		List<Sala> listaSala = salas.findByPredio(predio);

		

		TDHS tdhs = new TDHS();

		for(Sala sala : listaSala ) { 

			Ocupacao salaOcupacao = this.calcOcupacao(sala, semestreContexto);

			sala.setOcupacao(salaOcupacao);

			tdhs.setTotalDisponivelMatutino(salaOcupacao.getMatutino());
			tdhs.setTotalDisponivelVespertino(salaOcupacao.getVespertino());
			tdhs.setTotalDisponivelNoturno(salaOcupacao.getNoturno());
				
		}

		int totalSala = listaSala.size();

		ModelAndView modelView = new ModelAndView("ExibirTDHS");

		tdhs.setTotalHorarios(totalSala);
		tdhs.setTotalHorariosDiurno(totalSala);
		tdhs.setTotalHorariosNoturno(totalSala);
		tdhs.setTotalDisponivelGeral();
		
		System.out.println("TOTAL HORARIO" + tdhs.getTotalHorarios() );
		System.out.println("TOTAL DIRUNO" + tdhs.getTotalHorariosDiurno() );
		System.out.println("TOTAL NOTURNO" + tdhs.getTotalHorariosNoturno());
		System.out.println("TOTAL DISPONIVEL" + tdhs.getTotalDisponivelGeral());

		modelView.addObject("tdhs", tdhs);


		modelView.addObject("salas", listaSala);
		modelView.addObject("predio", predio);	
		modelView.addObject("width", 6);
		return   modelView;
	}


	@RequestMapping(value="/predio/{**}/tdhs/{sala}")
	public ModelAndView exibirTDHSSala(@PathVariable int sala,  @ModelAttribute("semestreContexto") Semestre  semestreContexto){
		ModelAndView modelView = new ModelAndView("SalaTDHS");



		Sala salaTDHS = salas.findOne(sala);

		Semestre semestreAtual = semestres.findOne(semestreContexto.getIdSemestre());

		List<Horario> horariosCadastrados = horarios.findBySala(salaTDHS);


		Ocupacao salaOcupacao = this.calcOcupacao(salaTDHS, semestreAtual);

		salaTDHS.setOcupacao(salaOcupacao);

		modelView.addObject("horariosCadastrados", horariosCadastrados);

		List<Planta> todasPlantas = plantas.findByPredio(salaTDHS.getPredio());
		
		Planta plantaInicial = plantas.findOne(salaTDHS.getPlanta().getIdPlanta());
		
		modelView.addObject("plantaInicial", plantaInicial);
		modelView.addObject("plantas", todasPlantas);
		modelView.addObject("sala", salaTDHS);

		//Informações estáticas
		modelView.addObject("todosHorarioPadrao", horarioPadrao.findBySemestreOrderByOrdemAsc(semestreAtual));


		return modelView;		
	}

	@RequestMapping(value="/tdhs/fragmento", method = RequestMethod.POST)
	public String fragmentoTDHS(Model model, @RequestParam("salaAtualizada") int idSala, @RequestParam("predioAtual") Predio predio, @RequestParam("salaNumero") String numeroSala, @RequestParam("semestreTDHS") int semestreTDHS ) { 

		Semestre semestreAtual = semestres.findOne(semestreTDHS);

		if( idSala != 0){  	
			Sala salaTDHS = salas.findOne(idSala);

			List<Horario> horariosCadastrados = horarios.findBySala(salaTDHS);


			Ocupacao salaOcupacao = this.calcOcupacao(salaTDHS, semestreAtual);

			salaTDHS.setOcupacao(salaOcupacao);


			model.addAttribute("horariosCadastrados", horariosCadastrados);
			model.addAttribute("sala", salaTDHS);

		} else { 

			Sala salaVazia = new Sala();

			salaVazia.setNumeroSala(numeroSala);
			List<Horario> horariosCadastrados  = new ArrayList<Horario>();



			model.addAttribute("sala", salaVazia);
			model.addAttribute("horariosCadastrados", horariosCadastrados);

		}

		//Informações estáticas
		model.addAttribute("todosHorarioPadrao", horarioPadrao.findBySemestreOrderByOrdemAsc(semestreAtual));


		return "TDHSFragmento";
	}



	public Ocupacao calcOcupacao(Sala sala, Semestre semestre) {

		Ocupacao salaOcupacao = new Ocupacao();

		int  total = horarios.findBySala(sala).size();
		int matutino = horarios.findBySalaAndTipoDescricao(sala, Turnos.MANHA.getTipo()).size();
		int vespertino = horarios.findBySalaAndTipoDescricao(sala, Turnos.TARDE.getTipo()).size();
		int noturno = horarios.findBySalaAndTipoDescricao(sala, Turnos.NOITE.getTipo()).size();

		salaOcupacao.setTotal(total);
		salaOcupacao.setMatutino(matutino);
		salaOcupacao.setVespertino(vespertino);
		salaOcupacao.setNoturno(noturno);


		return salaOcupacao;
	}


	@ModelAttribute("turnos")
	public Turnos[] turnos() {
		return Turnos.values();
	}

	@ModelAttribute("diasSemana")
	public DiasSemana[] diasSemana(){
		return DiasSemana.values();
	}

	@ModelAttribute("horarioOrdem")
	public HorararioOrdem[] horarioOrdem() {
		return HorararioOrdem.values();
	}


	@RequestMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();


		return "Login";
	}



	public static String makeUrl(HttpServletRequest request)
	{
		return request.getRequestURL().toString() + "?" + request.getQueryString();
	}



}
