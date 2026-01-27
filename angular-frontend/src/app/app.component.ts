import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [RouterOutlet, DashboardComponent],
    template: `
    <div class="app-container">
      <app-dashboard></app-dashboard>
    </div>
  `,
    styles: [`
    .app-container {
      min-height: 100vh;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      padding: 20px;
    }
  `]
})
export class AppComponent {
    title = 'Finance Tracker';
}
