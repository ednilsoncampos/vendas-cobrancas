package br.com.actusrota.enumerador;



public enum EnumCronometro {
	HORA {
		@Override
		public String mostrarTempo(long tempoInicial, Object threadName) {
			return null;
		}
	},
	MINUTOS {
		@Override
		public String mostrarTempo(long tempoInicial, Object threadName) {
			long tempoFinal = System.currentTimeMillis();
			System.out.println("minutos");
			return null;
		}
	},
	COMPLETO {
		@Override
		public String mostrarTempo(long tempoInicial, Object threadName) {
			long tempoFinal = System.currentTimeMillis();
			long tempoTotal = tempoFinal - tempoInicial;
			
			int milesegundos = (int) (tempoTotal % 1000);
			
			tempoTotal = tempoTotal / 1000;
			int segundos = (int) tempoTotal % 60;
			tempoTotal /= 60;
			
			int minutos = (int) (tempoTotal % 60);
			tempoTotal /= 60;
			
			segundos = segundos % 60;
			int hora = (int) tempoTotal;
			
			String msg = String.format("\n%s \t\t Hora:%d\t\t Minutos:%d\t Segundos:%d\t milisegundos:%d\n",
					threadName, hora, minutos, segundos, milesegundos);
			System.out.println(msg);
			return msg;
		}
	};
	
	private EnumCronometro() {
		
	}
	
	abstract String mostrarTempo(long tempoInicial, Object threadName);
	
	public String calcularTempo(long tempoInicial, Object threadName) {
		return this.mostrarTempo(tempoInicial, threadName);
	}
	
	private static long tempoTotal(long tempoTotal) {
		int milesegundos = (int) (tempoTotal % 1000);
		
		tempoTotal = tempoTotal / 1000;
		int segundos = (int) tempoTotal % 60;
		tempoTotal /= 60;
		
		int minutos = (int) (tempoTotal % 60);
		tempoTotal /= 60;
		
		segundos = segundos % 60;
		int hora = (int) tempoTotal;
		System.out.printf(" Hora:%d\t\t Minutos:%d\t Segundos:%d\t milisegundos:%d\n", hora, minutos, segundos, milesegundos);
		
		return tempoTotal;
	}
}
